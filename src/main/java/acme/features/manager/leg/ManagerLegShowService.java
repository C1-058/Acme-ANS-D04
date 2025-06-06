
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Leg;
import acme.entities.flight.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		status = leg != null && (!leg.getDraftMode() || super.getRequest().getPrincipal().hasRealm(leg.getFlight().getManager()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);

		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		//SelectChoices flightChoices;
		SelectChoices departureAirportChoices;
		SelectChoices arrivalAirportChoices;
		SelectChoices aircraftChoices;
		SelectChoices statusChoices;

		//List<Flight> flights = this.repository.findFlightsByManager(leg.getFlight().getManager().getId());
		//flightChoices = SelectChoices.from(flights, "tag", leg.getFlight());

		List<Airport> airports = this.repository.findAllAirports();
		departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());

		List<Aircraft> aircrafts = this.repository.findAllAircrafts(leg.getFlight().getManager().getAirline().getIataCode());
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());

		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumberDigits", "departure", "arrival", "status", "draftMode");

		dataset.put("flightNumber", leg.getFlightNumber());
		dataset.put("duration", leg.getDuration());
		dataset.put("statusChoices", statusChoices);
		dataset.put("published", !leg.getDraftMode());
		dataset.put("flight", leg.getFlight().getTag());
		//dataset.put("flights", flightChoices);
		dataset.put("aircraft", leg.getAircraft().getRegistrationNumber());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("departureAirport", leg.getDepartureAirport().getIataCode());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirport", leg.getArrivalAirport().getIataCode());
		dataset.put("arrivalAirports", arrivalAirportChoices);

		super.getResponse().addData(dataset);
	}

}
