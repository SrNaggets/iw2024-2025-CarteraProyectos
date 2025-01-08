package com.iw.IW.views;

import com.iw.IW.entities.Solicitud;
import com.iw.IW.repositories.SolicitudRepository;
import com.iw.IW.repositories.UsuarioRepository;
import com.iw.IW.services.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("carteraproyectos")
@PageTitle("Ver cartera de proyectos")
@RolesAllowed({"normal", "PROMOTOR", "CIO", "OTP"})
public class VerCarteraProyectosView extends VerticalLayout {

    public VerCarteraProyectosView(@Autowired SecurityService securityService, @Autowired SolicitudRepository solicitudRepository){
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.AUTO);

        Button logout = new Button("Logout", click -> securityService.logout());

        Button principal = new Button("Volver a menú principal", click -> getUI().ifPresent(ui -> ui.navigate("")));

        add(new HorizontalLayout(logout, principal));

        List<Solicitud> aux = solicitudRepository.findAll();

        add(new H2("Cartera de proyectos:"));

        if(aux.isEmpty()){
            add(new H4("No hay solicitudes en la cartera de proyectos."));
        }
        else{

            Grid<Solicitud> gridSolicitudes = new Grid<>(Solicitud.class, false);
            gridSolicitudes.addColumn(Solicitud::getTitulo).setHeader("Título");
            gridSolicitudes.addColumn(Solicitud::getInteresados).setHeader("Interesados");
            gridSolicitudes.addColumn(Solicitud::getEstado).setHeader("Estado");
            gridSolicitudes.addColumn(e -> e.getUsuario().getNombre()).setHeader("Solicitante");
            gridSolicitudes.addColumn(e -> e.getPromotor().getNombre()).setHeader("Promotor");
            gridSolicitudes.addColumn(Solicitud::getPrioridad).setHeader("Prioridad");

            gridSolicitudes.setItems(aux);

            add(gridSolicitudes);
        }
    }
}