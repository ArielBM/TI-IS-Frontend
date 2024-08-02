package com.jobbi.me.views.home;

import com.jobbi.me.entities.Header;
import com.jobbi.me.entities.Summary;
import com.jobbi.me.repositories.HeaderRepository;
import com.jobbi.me.repositories.SummaryRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Home")
@Route("home")
@PermitAll
@org.springframework.stereotype.Component
@Scope("prototype")
@JsModule("./src/chart-component.js")
public class HomeView extends VerticalLayout {

    private static final String LOGOUT_SUCCESS_URL = "/";
    private final HeaderRepository headerRepository;
    private final SummaryRepository summaryRepository;

    @Autowired
    public HomeView(HeaderRepository headerRepository, SummaryRepository summaryRepository) {

        add(logoutSection());

        this.headerRepository = headerRepository;
        this.summaryRepository = summaryRepository;

        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2("TI-IS Technical Test");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("Select a header"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        add(searchSection());
        add(createGenderChart());
    }

    private Component searchSection() {

        ComboBox<Header> headersCB = new ComboBox<>("Header");
        headersCB.setItems(this.headerRepository.findAll());
        headersCB.setItemLabelGenerator(header -> header.getDate().toString());

        headersCB.addValueChangeListener(event -> {
           Header selectedHeader = event.getValue();
           if (selectedHeader != null) {
               updateGraphs(selectedHeader);
           }
        });

        FlexLayout content = new FlexLayout(headersCB);
        content.setWidthFull();
        content.setJustifyContentMode(JustifyContentMode.CENTER);

        return  content;
    }

    private Component logoutSection() {
        Button logoutBtn = new Button("Logout", new Icon(VaadinIcon.POWER_OFF));
        logoutBtn.addClickListener(event -> {
            UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(
                    VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                    null);
        });

        HorizontalLayout content = new HorizontalLayout(logoutBtn);
        content.setWidthFull();
        content.setJustifyContentMode(JustifyContentMode.END);

        return content;
    }

    private void updateGraphs(Header selectedHeader) {
        List<Summary> summaryList = summaryRepository.findByHeaderId(selectedHeader.getId()).stream().toList();
        if (summaryList.size() > 0) {
            List<Summary> gendersList = summaryList.stream().filter(item -> item.getCategory().equals("Gender")).collect(Collectors.toList());
            if (gendersList.size() > 0) {
                String data = gendersList.stream()
                        .map(summary -> String.valueOf(summary.getCount()))
                        .collect(Collectors.joining(", ", "[", "]"));

                String labels = gendersList.stream()
                        .map(summary -> "\"" + summary.getSubcategory() + "\"")
                        .collect(Collectors.joining(", ", "[", "]"));

                UI.getCurrent().getPage().executeJs("updateChartData($0, $1, $2)","genderChart", data, labels);

            }

            List<Summary> osList = summaryList.stream().filter(item -> item.getCategory().equals("Os")).collect(Collectors.toList());
            if (osList.size() > 0) {
                String data = osList.stream()
                        .map(summary -> String.valueOf(summary.getCount()))
                        .collect(Collectors.joining(", ", "[", "]"));

                String labels = osList.stream()
                        .map(summary -> "\"" + summary.getSubcategory() + "\"")
                        .collect(Collectors.joining(", ", "[", "]"));

                UI.getCurrent().getPage().executeJs("updateChartData($0, $1, $2)","osChart", data, labels);

            }
        }
    }

    private Component createGenderChart() {

        FlexLayout chartGenderContainer = new FlexLayout();
        chartGenderContainer.setId("chartContainer");
        chartGenderContainer.getElement().setProperty("innerHTML", "<chart-component chartId='genderChart' chartTitle='Gender Distribution'></chart-component>");
        chartGenderContainer.setAlignSelf(Alignment.CENTER);
        chartGenderContainer.setWidth("100%");
        chartGenderContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        FlexLayout chartSoContainer = new FlexLayout();
        chartSoContainer.setId("chartContainer2");
        chartSoContainer.getElement().setProperty("innerHTML", "<chart-component chartId='osChart' chartTitle='Os Distribution'></chart-component>");
        chartSoContainer.setAlignSelf(Alignment.CENTER);
        chartSoContainer.setWidth("100%");
        chartSoContainer.setJustifyContentMode(JustifyContentMode.CENTER);


        return new VerticalLayout(chartGenderContainer,chartSoContainer);
    }

    private void consoleLog(String message) {
        UI.getCurrent().getPage().executeJs(String.format("console.log('%s')", message));
    }

}
