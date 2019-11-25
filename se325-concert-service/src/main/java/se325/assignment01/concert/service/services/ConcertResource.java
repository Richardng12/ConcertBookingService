package se325.assignment01.concert.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se325.assignment01.concert.common.dto.*;
import se325.assignment01.concert.common.types.BookingStatus;
import se325.assignment01.concert.service.domain.*;
import se325.assignment01.concert.service.jaxrs.LocalDateTimeParam;
import se325.assignment01.concert.service.mapper.*;
import se325.assignment01.concert.service.util.ConcertInfoSubscription;
import se325.assignment01.concert.service.util.TheatreLayout;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/concert-service")
public class ConcertResource {

    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    //Holds a list of ConcertInfoSubscriptionLists that can be iterated through
    private static final List<ConcertInfoSubscription> concertInfoSubscriptionLists = new ArrayList<>();

    public static final double totalNumberOfSeats = 120.0;

    /**
     * This method takes a concert id and returns a ConcertDTO object for that id
     * @param id
     * @return
     */
    @GET
    @Path("concerts/{ID}")
    public Response retrieveConcert(@PathParam("ID") long id) {

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            em.getTransaction().begin();

            //Find the concert with the id parsed in
            Concert concert = em.find(Concert.class, id);

            //Return HTTP 404 response if the specified Concert is not found
            if (concert == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            em.getTransaction().commit();
            //Return successful DTO concert found
            return Response.ok(ConcertMapper.convertToDTO(concert)).build();
        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }
    }

    /**
     * This method gets all concerts in the database
     * @return
     */
    @GET
    @Path("concerts")
    public Response retrieveAllConcerts() {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        List<Concert> concertsRetrieved;

        try {

            em.getTransaction().begin();

            //Get all concerts in the database
            TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class);
            concertsRetrieved = concertQuery.getResultList();


            //No concerts in database
            if(concertsRetrieved.isEmpty()){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            em.getTransaction().commit();

            List<ConcertDTO> concertsRetrievedDTO = new ArrayList<ConcertDTO>();

            GenericEntity<List<ConcertDTO>> entity = new GenericEntity<List<ConcertDTO>>(concertsRetrievedDTO) {
            };

            //Convert Concert to ConcertDTO
            for (Concert c : concertsRetrieved) {
                concertsRetrievedDTO.add(ConcertMapper.convertToDTO(c));
            }

            return Response.ok(entity).build();
        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }
    }


    /**
     * This method gets all concert summaries in the database
     * @return
     */
    @GET
    @Path("concerts/summaries")
    public Response retrieveAllConcertSummaries() {

        EntityManager em = PersistenceManager.instance().createEntityManager();


        List<Concert> concertsRetrieved;

        try {

            em.getTransaction().begin();

            //Get all Concerts
            TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class);
            concertsRetrieved = concertQuery.getResultList();

            //No concerts in database
            if(concertsRetrieved.isEmpty()){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            em.getTransaction().commit();


            List<ConcertSummaryDTO> concertSummariesRetrievedDTO = new ArrayList<ConcertSummaryDTO>();

            GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<List<ConcertSummaryDTO>>(concertSummariesRetrievedDTO) {
            };

            //Convert Concerts to ConcertSummaryDTOS
            for (Concert c : concertsRetrieved) {
                concertSummariesRetrievedDTO.add(ConcertMapper.convertToSummaryDTO((c)));
            }

            return Response.ok(entity).build();
        } finally {
            em.close();
        }
    }

    /**
     * This method takes in an id parameter and returns a performer for that id
     * @param id
     * @return
     */
    @GET
    @Path("performers/{ID}")
    public Response retrievePerformer(@PathParam("ID") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();

            //Get the performer associated with the id
            Performer performer = em.find(Performer.class, id);
            em.getTransaction().commit();

            //Return HTTP 404 response if the specified Performer is not found
            if (performer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok(PerformerMapper.convertToDTO(performer)).build();
        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }

    }

    /**
     * This method returns a list of all performers in the database
     * @return
     */
    @GET
    @Path("performers")
    public Response retrieveAllPerformers() {
        EntityManager em = PersistenceManager.instance().createEntityManager();


        List<Performer> performersRetrieved = new ArrayList<Performer>();

        try {

            em.getTransaction().begin();

            //Get the resullting list of performers
            TypedQuery<Performer> performerQuery = em.createQuery("select p from Performer p", Performer.class);
            performersRetrieved = performerQuery.getResultList();

            //No Performers in database
            if(performersRetrieved.isEmpty()){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            em.getTransaction().commit();

            //Convert the domain class to DTO and send it over
            List<PerformerDTO> performersRetrievedDTO = new ArrayList<PerformerDTO>();

            GenericEntity<List<PerformerDTO>> entity = new GenericEntity<List<PerformerDTO>>(performersRetrievedDTO) {
            };
            for (Performer c : performersRetrieved) {
                performersRetrievedDTO.add(PerformerMapper.convertToDTO(c));
            }


            return Response.ok(entity).build();
        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }

    }

    /**
     * This method authenticates the user, if the user doesn't currently have a token, it assigns a token
     * @param userDTO
     * @return
     */
    @POST
    @Path("/login")
    public Response authenticateUser(UserDTO userDTO) {

        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();

            //If userDTO is invalid, return bad request
            if (userDTO.getUsername() == null || userDTO.getPassword() == null
                    || userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            TypedQuery<User> getUserQuery = em.createQuery("select u from User u where u.username = :username " +
                    "AND u.password = :password ", User.class)
                    .setParameter("username", userDTO.getUsername())
                    .setParameter("password",userDTO.getPassword());

            User user;

            //Check if user and pass map to any users
            if(getUserQuery.getResultList().isEmpty()){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }else{
                user = getUserQuery.getSingleResult();
            }

            //Check if user is authenticated
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (!userDTO.getUsername().equals(user.getUsername())) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (!userDTO.getPassword().equals(user.getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            //If user doesn't have token, resgister him/her with a token
            if (user.getToken() == null) {
                UUID token = UUID.randomUUID();
                user.setToken(token.toString());
            }

            em.getTransaction().commit();
            return Response.ok().entity(UserMapper.convertToDTO(user)).cookie(new NewCookie("auth", user.getToken())).build();
        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }
    }

    /**
     * This method takes in a booking request and a token, and makes a booking depending on the authorisation level of the user
     * No bookings can be made for seats that are already booked
     * @param bookingRequestDTO
     * @param token
     * @return
     */
    @POST
    @Path("/bookings")
    public Response makeBooking(BookingRequestDTO bookingRequestDTO, @CookieParam("auth") Cookie token) {

        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();

            //check auth code
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //Get user associated with token
            TypedQuery<User> getUserQuery = em.createQuery("select u from User u where u.token = :token", User.class)
                    .setParameter("token", token.getValue());
            User user = getUserQuery.getSingleResult();

            //if user is null, token is incorrect
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //Get concert associated with the date
            TypedQuery<Concert> concertQuery = em.createQuery("SELECT c FROM Concert c " +
                    "WHERE c.id = :id", Concert.class)
                    .setParameter("id", bookingRequestDTO.getConcertId());

            //Check if the resulting concert list is empty
            if (concertQuery.getResultList().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Concert concert = concertQuery.getSingleResult();

            //Check concert exists for that particular time
            if (concert == null || (!concert.getDates().contains(bookingRequestDTO.getDate()))) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            //Get all  seats for concert at the specified time (since only one concert per date)
            TypedQuery<Seat> seatsForConcert = em.createQuery("SELECT s FROM Seat s " +
                    "WHERE s.dateTime = :date", Seat.class)
                    .setParameter("date", bookingRequestDTO.getDate())
                    .setLockMode(LockModeType.OPTIMISTIC);

            if (seatsForConcert.getResultList().isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }

            //Get all the seats for that concert at that date
            List<Seat> allSeatsForConcert = seatsForConcert.getResultList();

            //Get just the booked seats that user has chosen to book (via seat label)
            List<Seat> bookedSeats = new ArrayList<>();
            for (Seat s : allSeatsForConcert) {
                if ((bookingRequestDTO.getSeatLabels().contains(s.getLabel()))) {
                    bookedSeats.add(s);
                }
            }

            //Get all seats which have already been booked for that concert at the specified time
            TypedQuery<Seat> alreadyBookedSeatsQuery = em.createQuery("SELECT s FROM Seat s " +
                    "WHERE s.dateTime = :date AND s.bookingStatus = :bookingStatus", Seat.class)
                    .setParameter("date", bookingRequestDTO.getDate())
                    .setParameter("bookingStatus", BookingStatus.Booked);


            List<Seat> alreadyBookedSeats;

            //If all seats are free, set alreadyBookedSeats to an empty array list, else set alreadyBookedSeats to just
            //the seats which have already been booked
            if (!alreadyBookedSeatsQuery.getResultList().isEmpty()) {
                alreadyBookedSeats = alreadyBookedSeatsQuery.getResultList();
            } else {
                alreadyBookedSeats = new ArrayList<>();
            }

            //Return duplicates if the user is trying to book a seat that has already been booked
            alreadyBookedSeats.retainAll(bookedSeats);

            //if there is matching seats, make the request forbidden
            if (!alreadyBookedSeats.isEmpty()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            //Convert the list to a set so we can instantiate the Booking Object
            Set<Seat> bookedSeatsSet = new HashSet<>();


            //Set the seats as booked
            for (Seat seat : bookedSeats) {
                seat.setBooked(true);
                seat.setBookingStatus(BookingStatus.Booked);
                bookedSeatsSet.add(seat);
                em.merge(seat);
            }


            //*** PUBLISH/SUBSCRIBE START ****

            //Get our percentage of currently booked seats
            int numberOfSeatsBooked = alreadyBookedSeatsQuery.getResultList().size();
            double percentageBooked = (numberOfSeatsBooked)/totalNumberOfSeats * 100;

            List<ConcertInfoSubscription> removingList = new ArrayList<>();

            //Synchronized currency (only one thread can access the list at a time)
            synchronized (concertInfoSubscriptionLists) {

                //for all subscriptions
                for (ConcertInfoSubscription cSub : concertInfoSubscriptionLists) {

                    //if current booking  matches the subscription (via date and id)
                    if (bookingRequestDTO.getConcertId() == cSub.getConcertId() && bookingRequestDTO.getDate().equals(cSub.getDate())) {

                        //if percentage booked is greater or equal to the percentage booked user has inputted
                        if (percentageBooked >= cSub.getPercentageBooked()) {

                            //notify the user of the remaining number of seats
                            notifyConcertInfoSubscription(numberOfSeatsBooked, cSub);
                            removingList.add(cSub);
                        }
                    }
                }
                //once notified, remove the subscription from the list
                concertInfoSubscriptionLists.remove(removingList);
            }

            //**** PUBLISH/SUBSCRIBE END ****


            //Create the new booking
            Booking newBooking = new Booking(user, concert, bookingRequestDTO.getDate(), bookedSeatsSet);

            //persist the added booking into the User table.
            user.getBookings().add(newBooking);
            em.persist(newBooking);


            em.getTransaction().commit();

            //return the response to the specified booking id
            return Response.created(URI.create("/concert-service/bookings/" + newBooking.getBookingID())).build();


        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }
    }


    /**
     * This method gets seats on a particular date depending on the status given
     * @param dateParam
     * @param status
     * @return
     */
    @GET
    @Path("/seats/{DATE}")
    public Response getSeats(@PathParam("DATE") LocalDateTimeParam dateParam, @QueryParam("status") BookingStatus status) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();

            //get the date from the queryParam
            LocalDateTime date = dateParam.getLocalDateTime();

            List<Seat> seatsRetrieved = new ArrayList<>();

            //Depending on the status, get the relevant number of seats
            if (status.equals(BookingStatus.Booked)) {
                TypedQuery<Seat> seatQuery = em.createQuery("SELECT s FROM Seat s where s.dateTime = :date AND s.bookingStatus = :bookingStatus", Seat.class)
                        .setParameter("date", date)
                        .setParameter("bookingStatus", BookingStatus.Booked);

                seatsRetrieved = seatQuery.getResultList();
            } else if (status.equals(BookingStatus.Unbooked)) {

                TypedQuery<Seat> seatQuery = em.createQuery("SELECT s FROM Seat s where s.dateTime = :date AND s.bookingStatus = :bookingStatus", Seat.class)
                        .setParameter("date", date)
                        .setParameter("bookingStatus", BookingStatus.Unbooked);

                seatsRetrieved = seatQuery.getResultList();
            } else if (status.equals(BookingStatus.Any)) {
                TypedQuery<Seat> seatQuery = em.createQuery("SELECT s FROM Seat s where s.dateTime = :date", Seat.class)
                        .setParameter("date", date);

                seatsRetrieved = seatQuery.getResultList();
            }

            em.getTransaction().commit();

            List<SeatDTO> seatsRetrievedDTO = new ArrayList<SeatDTO>();
            GenericEntity<List<SeatDTO>> entity = new GenericEntity<List<SeatDTO>>(seatsRetrievedDTO) {
            };

            //Concert Seats to SeatDTOS
            for (Seat s : seatsRetrieved) {
                seatsRetrievedDTO.add(SeatMapper.convertToDTO(s));
            }

            return Response.ok(entity).build();

        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }
    }


    /**
     * This method takes in a bookingID and a token, and retrieves the booking for that id, a user should only be able to
     * look at their own bookings
     * @param bookingId
     * @param token
     * @return
     */
    @GET
    @Path("bookings/{ID}")
    public Response getBookingById(@PathParam("ID") long bookingId, @CookieParam("auth") Cookie token) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            em.getTransaction().begin();

            //check auth code
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //Get user associated with token
            TypedQuery<User> getUserQuery = em.createQuery("select u from User u where u.token = :token", User.class)
                    .setParameter("token", token.getValue());
            User user = getUserQuery.getSingleResult();

            //if user is null, token is incorrect
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //Find the booking given the booking id
            Booking booking = em.find(Booking.class, bookingId);

            //Make sure user can only access their own bookings
            if (!booking.getUser().equals(user)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            //
            if (booking == null) {
                //Return HTTP 404 response if the specified Booking is not found
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok(BookingMapper.convertToDTO(booking)).build();
        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.getTransaction().commit();
            em.close();
        }
    }


    /**
     * This method gets all bookings that a user has made
     * @param token
     * @return
     */
    @GET
    @Path("/bookings")
    public Response getAllBookings(@CookieParam("auth") Cookie token) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {

            em.getTransaction().begin();

            //check auth code
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //Get user associated with token
            TypedQuery<User> getUserQuery = em.createQuery("select u from User u where u.token = :token", User.class)
                    .setParameter("token", token.getValue());
            User user = getUserQuery.getSingleResult();
            em.getTransaction().commit();
            //if user is null, token is incorrect
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            List<Booking> bookingsRetrieved;

            //Get all bookings the user has made
            bookingsRetrieved = user.getBookings();

            List<BookingDTO> bookingsRetrievedDTO = new ArrayList<>();


            GenericEntity<List<BookingDTO>> entity = new GenericEntity<List<BookingDTO>>(bookingsRetrievedDTO) {
            };

            //Convert Bookings to BookingsDTO
            for (Booking b : bookingsRetrieved) {
                bookingsRetrievedDTO.add(BookingMapper.convertToDTO(b));
            }
            return Response.ok(entity).build();
        } catch (RollbackException e) {
            return Response.serverError().build();
        } finally {
            em.close();
        }
    }

    /**
     * This method adds a AsyncResponse to a ConcertInfoSubscription, the user has to be authenticated.
     *
     * @param sub
     */
    @POST
    @Path("/subscribe/concertInfo")
    public void subscribeToConcertInfoSubscriptions(ConcertInfoSubscriptionDTO concertInfoSubscriptionDTO, @Suspended AsyncResponse sub, @CookieParam("auth") Cookie token) {

        //Check for authentication
        if (token == null) {
            sub.resume(Response.status(Response.Status.UNAUTHORIZED).build());
        }else{
            EntityManager em = PersistenceManager.instance().createEntityManager();

            try{
                em.getTransaction().begin();

                Concert concert = em.find(Concert.class,concertInfoSubscriptionDTO.getConcertId());

                //Check concert exists for that particular time
                if (concert == null || (!concert.getDates().contains(concertInfoSubscriptionDTO.getDate()))) {
                    sub.resume(Response.status(Response.Status.BAD_REQUEST).build());
                }

                //Convert the ConcertInfoSubscriptionDTO to the helper class
                ConcertInfoSubscription cInfoSubscription = ConcertInfoSubscriptionMapper.convertToDomain(concertInfoSubscriptionDTO);

                //Set the AsyncResponse in the concertInfoSub class
                cInfoSubscription.setConcertAsyncResponse(sub);

                //Add the ConcertInfoSubscription to the ArrayList
                concertInfoSubscriptionLists.add(cInfoSubscription);

            }catch(RollbackException e){
                sub.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            }
            finally{
                em.close();
            }
        }
    }


    /**
     * Notifies the client of the remaining number of concerts by sending over a ConcertInfoNotificationDTO
     * @param numberOfSeatsBooked
     * @param concertInfoSubscription
     */
    private void notifyConcertInfoSubscription(int numberOfSeatsBooked, ConcertInfoSubscription concertInfoSubscription){

        //Crate the NotificationDTO, taking in the number of seats currently remaining
        ConcertInfoNotificationDTO concertInfoNotificationDTO = new ConcertInfoNotificationDTO
                (TheatreLayout.NUM_SEATS_IN_THEATRE - numberOfSeatsBooked);

        //Get the async response and resume the ConcertInfoNotificationDTO
        concertInfoSubscription.getConcertAsyncResponse().resume(concertInfoNotificationDTO);
    }

}


