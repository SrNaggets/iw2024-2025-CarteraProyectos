package com.iw.IW.views;

import com.iw.IW.entities.EvaluacionTecnica;
import com.iw.IW.entities.Solicitud;
import com.iw.IW.repositories.SolicitudRepository;
import com.iw.IW.services.EvaluacionTecnicaService;
import com.iw.IW.services.SolicitudService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.List;

@Route("evaluaciontecnica")
@PageTitle("Evaluación técnica de solicitud")
@RolesAllowed({"OTP"})
public class TecnicaView extends VerticalLayout implements HasUrlParameter<Long> {


    @Autowired
    SolicitudRepository solicitudRepository;

    @Autowired
    EvaluacionTecnicaService evaluacionTecnicaService;

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        if (solicitudRepository.findById(parameter).isPresent()) {
            Solicitud solicitud = solicitudRepository.findById(parameter).get();

            Button volver = new Button("Volver a solicitudes");
            volver.addClickListener(e -> {
                getUI().ifPresent(ui -> ui.navigate("/evaluaciontecnica/"));
            });
            add(volver);


            add(new H3("Título: " + solicitud.getTitulo()),

                    new H4("Importancia para el promotor: " + solicitud.getImportanciaPromotor()),

                    new H4("Interesados en el proyecto: " + solicitud.getInteresados()),
                    new H4("Financiación aportada: " + solicitud.getOros() + "€"),

                    new H3("Justificación del proyecto:"));

            List<String> alineamientos = new java.util.ArrayList<>(List.of());

            if (solicitud.getAli1() == 1)
                alineamientos.add("innovar, rediseñar y actualizar nuestra oferta formativa para adaptarla a las necesidades sociales y económicas de nuestro entorno");
            if (solicitud.getAli2() == 1)
                alineamientos.add("conseguir los niveles más altos de calidad en nuestra oferta formativa propia y reglada");
            if (solicitud.getAli3() == 1)
                alineamientos.add("aumentar significativamente nuestro posicionamiento en investigación y transferir de forma relevante y útil nuestra investigación a nuestro tejido social y productivo");
            if (solicitud.getAli4() == 1)
                alineamientos.add("consolidar un modelo de gobierno sostenible y socialmente responsable");
            if (solicitud.getAli5() == 1)
                alineamientos.add("conseguir que la transparencia sea un valor distintivo y relevante en la UCA");
            if (solicitud.getAli6() == 1) alineamientos.add("generar valor compartido con la Comunidad Universitaria");
            if (solicitud.getAli7() == 1)
                alineamientos.add("reforzar la importancia del papel de la UCA en la sociedad");

            String alisFinal = String.join(", ", alineamientos);
            alisFinal = String.join("", alisFinal, ".");
            alisFinal = alisFinal.substring(0, 1).toUpperCase() + alisFinal.substring(1);

            add(new Paragraph(alisFinal));

            add(
                    new H4("Número de personas que se beneficiarían de la implantación del proyecto: " + solicitud.getAlcance()),


                    new H3("Archivos adjuntos"));

            StreamResource memoriaArchivo = new StreamResource("memoria.pdf", () -> new ByteArrayInputStream(solicitud.getMemoria()));

            Anchor enlaceMemoria = new Anchor(memoriaArchivo, "Memoria");

            StreamResource tecnicoArchivo = new StreamResource("especificaciones.pdf", () -> new ByteArrayInputStream(solicitud.getTecnico()));

            Anchor enlaceTecnico = new Anchor(tecnicoArchivo, "Especificaciones técnicas");

            StreamResource presupuestosArchivo = new StreamResource("presupuestos.pdf", () -> new ByteArrayInputStream(solicitud.getPresupuesto()));

            Anchor enlacePresupuestos = new Anchor(presupuestosArchivo, "Presupuestos");

            add(
                    enlaceMemoria,
                    enlaceTecnico,
                    enlacePresupuestos);


            TextField Alineamiento = new TextField("Idoneidad técnica:");
            Alineamiento.addClassName("bordered");
            Alineamiento.setPlaceholder("");
            Alineamiento.setWidth("70%");

            TextField RecursosH = new TextField("Recursos humanos necesarios:");
            RecursosH.addClassName("bordered");
            RecursosH.setPlaceholder("");
            RecursosH.setWidth("70%");

            NumberField RecursosF = new NumberField("Recursos económicos necesarios:");
            RecursosF.addClassName("bordered");
            RecursosF.setPlaceholder("");
            RecursosF.setWidth("70%");

            Button enviar = new Button("Enviar evaluación técnica", e -> {

                EvaluacionTecnica evaluacionTecnica = new EvaluacionTecnica();
                evaluacionTecnica.setAlineamiento(Alineamiento.getValue());
                evaluacionTecnica.setRecursosF(RecursosF.getValue().longValue());
                evaluacionTecnica.setRecursosH(RecursosH.getValue());

                evaluacionTecnicaService.registrarEvaluacionTecnica(solicitud.getId(), evaluacionTecnica);

                getUI().ifPresent(ui -> ui.navigate("/avalar/"));
            });

            add(Alineamiento, RecursosH, RecursosF, enviar);


        } else {
            throw new RuntimeException("Error: no se encontró solicitud");
        }

    }
}