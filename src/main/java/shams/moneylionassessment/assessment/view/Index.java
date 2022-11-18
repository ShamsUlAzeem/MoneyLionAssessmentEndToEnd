package shams.moneylionassessment.assessment.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import shams.moneylionassessment.assessment.security.SecurityService;

@Route("/")
@PageTitle("Dashboard")
public class Index extends AppLayout {

    private final transient SecurityService securityService;

    public Index(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("MoneyLion Assessment Dashboard");
        logo.addClassNames("text-l", "m-m");

        Button logout = new Button("Log out", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

}