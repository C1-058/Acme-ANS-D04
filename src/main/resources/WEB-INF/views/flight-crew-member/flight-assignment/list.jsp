<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.duty" path="duty" width="30%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.lastUpdate" path="moment" width="30%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.status" path="status" width="30%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.leg" path="leg" width="20%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.draftMode" path="draftMode" width="20%"/>

	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
