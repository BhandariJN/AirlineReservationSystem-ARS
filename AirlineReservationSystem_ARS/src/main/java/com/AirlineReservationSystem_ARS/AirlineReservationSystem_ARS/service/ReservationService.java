package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.ReservationStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.ReservationRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.ReservationRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ReservationResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user.AirlineUserDetails;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final FlightService flightService;
    private final FlightRepo flightRepo;
    private final ReservationRepo reservationRepo;
    private final UserRepo userRepo;

    public ReservationResponse reserveFlight(ReservationRequest reservationRequest) {


        Flight flight = flightExists(reservationRequest);
        seatAvalibilityCheck(reservationRequest);

        Reservation reservation = new Reservation();
        // get user form security context holder
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(userDetails.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setNoOfPassengers(reservationRequest.getNoOfPassengers());
        reservation.setTotalFare(reservationRequest.getTotalFare());
        reservation.setPnr(generatePNR());

        return Reservation.toReservationResponse(reservationRepo.save(reservation));
    }

    public void cancelReservation(String pnr) {
        Reservation reservation = Optional.ofNullable(reservationRepo.findByPnr(pnr)).orElseThrow(
                () -> new ResourceNotFoundException("Reservation not found")
        );
        reservationRepo.delete(reservation);
    }

    public byte[] downloadTicket(String pnr) {
        // Define formatters
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Fetch reservation
            Reservation reservation = Optional.ofNullable(reservationRepo.findByPnr(pnr))
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

            // Create PDF document
            Document document = new Document(PageSize.A4, 36, 36, 72, 72); // margins
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add styling
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(0, 102, 204)); // Darker blue
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
            Font highlightFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(51, 51, 51));

            // Add header with airline name instead of logo
            Paragraph airlineHeader = new Paragraph(
                    reservation.getFlight().getManagedBy().getName().toUpperCase(),
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0, 51, 102))
            );
            airlineHeader.setAlignment(Element.ALIGN_CENTER);
            airlineHeader.setSpacingAfter(5);
            document.add(airlineHeader);

            // Add header
            Paragraph header = new Paragraph("E-TICKET RECEIPT", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            // Add booking info section
            Paragraph bookingHeader = new Paragraph("BOOKING INFORMATION", titleFont);
            bookingHeader.setSpacingAfter(10);
            document.add(bookingHeader);

            // Create a table for booking info with light blue background for header
            PdfPTable bookingTable = new PdfPTable(2);
            bookingTable.setWidthPercentage(100);
            bookingTable.setSpacingBefore(10);
            bookingTable.setSpacingAfter(15);
            bookingTable.setWidths(new float[]{1, 2});

            addTableRow(bookingTable, "Booking Reference:", reservation.getPnr(), subtitleFont, highlightFont);
            addTableRow(bookingTable, "Issued On:",
                    reservation.getReservationDate().format(dateFormatter), subtitleFont, normalFont);
            addTableRow(bookingTable, "Passenger Name:",
                    reservation.getUser().getName().toUpperCase(), subtitleFont, highlightFont);
            addTableRow(bookingTable, "Number of Passengers:",
                    String.valueOf(reservation.getNoOfPassengers()), subtitleFont, normalFont);

            document.add(bookingTable);

            // Add flight info section
            Paragraph flightHeader = new Paragraph("FLIGHT DETAILS", titleFont);
            flightHeader.setSpacingAfter(10);
            document.add(flightHeader);

            // Calculate arrival time
            LocalDateTime departureTime = reservation.getFlight().getFlightSchedule().getDepartureTime();
            int hrs = reservation.getFlight().getFlightSchedule().getJourneyHrs();
            int mins = reservation.getFlight().getFlightSchedule().getJourneyMins();
            LocalDateTime arrivalTime = departureTime.plusHours(hrs).plusMinutes(mins);

            // Create flight info table
            PdfPTable flightTable = new PdfPTable(2);
            flightTable.setWidthPercentage(100);
            flightTable.setSpacingBefore(10);
            flightTable.setSpacingAfter(15);
            flightTable.setWidths(new float[]{1, 2});

            addTableRow(flightTable, "Airline:", reservation.getFlight().getManagedBy().getName(),
                    subtitleFont, highlightFont);
            addTableRow(flightTable, "Departure:",
                    String.format("%s at %s",
                            reservation.getFlight().getFlightSchedule().getRouteInfo().getOrigin(),
                            departureTime.format(timeFormatter)),
                    subtitleFont, normalFont);
            addTableRow(flightTable, "Arrival:",
                    String.format("%s at %s",
                            reservation.getFlight().getFlightSchedule().getRouteInfo().getDestination(),
                            arrivalTime.format(timeFormatter)),
                    subtitleFont, normalFont);
            addTableRow(flightTable, "Flight Duration:",
                    String.format("%d hrs %d mins", hrs, mins), subtitleFont, normalFont);
            addTableRow(flightTable, "Travel Date:",
                    departureTime.format(dateFormatter), subtitleFont, normalFont);

            document.add(flightTable);

            // Add footer
            Paragraph footer = new Paragraph("Thank you for choosing " +
                    reservation.getFlight().getManagedBy().getName() +
                    ". Have a pleasant journey!",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.DARK_GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            // Add barcode (with error handling)
            try {
                PdfContentByte cb = writer.getDirectContent();
                Barcode128 barcode = new Barcode128();
                barcode.setCode(reservation.getPnr());
                barcode.setBarHeight(50);
                Image barcodeImage = barcode.createImageWithBarcode(cb, BaseColor.BLACK, BaseColor.WHITE);
                barcodeImage.setAlignment(Element.ALIGN_CENTER);
                document.add(barcodeImage);
            } catch (Exception e) {
                // Fallback if barcode generation fails
                Paragraph barcodeFallback = new Paragraph("Booking Ref: " + reservation.getPnr(),
                        new Font(Font.FontFamily.COURIER, 12, Font.BOLD, BaseColor.BLACK));
                barcodeFallback.setAlignment(Element.ALIGN_CENTER);
                document.add(barcodeFallback);
            }

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ticket PDF: " + e.getMessage(), e);
        }
    }

    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorderColor(BaseColor.LIGHT_GRAY);
        labelCell.setPadding(5);
        labelCell.setBackgroundColor(new BaseColor(240, 240, 240)); // Light gray background

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorderColor(BaseColor.LIGHT_GRAY);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    public String generatePNR() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 12);
    }


    public List<ReservationResponse> getAllReservationofUser() {
        AirlineUserDetails airlineUserDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        List<Reservation> reservations = Optional.ofNullable(reservationRepo.findAllByUser_UserId(airlineUserDetails.getId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Reservation not found")
                );


        return reservations.stream().map(Reservation::toReservationResponse).sorted(Comparator.comparing(
                ReservationResponse::getReservationDate).reversed()
        ).toList();

    }

    public String makePayment(String pnr) {
        Reservation reservation = Optional.of(reservationRepo.findByPnr(pnr)).orElseThrow(
                () -> new ResourceNotFoundException("Reservation not found")
        );
        switch (reservation.getStatus()) {
            case PENDING: {
                reservation.setStatus(ReservationStatus.CONFIRMED);
                reservationRepo.save(reservation);
                return "Payment of " + reservation.getPnr() + " has been paid successfully";

            }
            case CONFIRMED: {
                throw new AlreadyExistException("Reservation is already confirmed");
            }
            case CANCELLED: {
                throw new AlreadyExistException("Reservation is already cancelled");
            }
            default:
                return null;
        }


    }

    public List<ReservationResponse> getAllAirlineReservation() {
        AirlineUserDetails airlineUserDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = airlineUserDetails.getEmail();


        return null;

    }


    public Flight flightExists(ReservationRequest reservationRequest) {
        return flightRepo.findByFlightNumber(reservationRequest.getFlightNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Flight not found")
        );
    }

    public void seatAvalibilityCheck(ReservationRequest reservationRequest) {
        Flight flight = flightExists(reservationRequest);
        Long capacity = flight.getFlightSchedule().getAirbus().getCapacity();
        Long reservedSeats = flight.getReservations().stream().mapToLong(
                Reservation::getNoOfPassengers
        ).sum();
        Long requestedSeats = reservationRequest.getNoOfPassengers();
        if (reservedSeats + requestedSeats > capacity) {
            throw new AlreadyExistException("Sorry!!  Requested " + reservationRequest.getNoOfPassengers() + " seats are not available right now!");
        }

    }


}
