package shams.moneylionassessment.assessment.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import shams.moneylionassessment.assessment.data.models.Feature;
import shams.moneylionassessment.assessment.data.models.User;
import shams.moneylionassessment.assessment.security.SecurityService;
import shams.moneylionassessment.assessment.service.FeatureService;
import shams.moneylionassessment.assessment.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Route("")
@PageTitle("Dashboard")
@RolesAllowed("ADMIN")
public class Index extends VerticalLayout {

    private final transient SecurityService securityService;
    private final transient FeatureService featureService;
    private final transient UserService userService;

    public Index(SecurityService securityService, FeatureService featureService, UserService userService) {
        this.securityService = securityService;
        this.featureService = featureService;
        this.userService = userService;
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("MoneyLion Assessment Dashboard");
        logo.addClassNames("text-l", "m-m");

        AtomicReference<User> selectedUser = new AtomicReference<>();
        AtomicReference<Feature> selectedFeature = new AtomicReference<>();

        var assignFeaturesGrid = new Grid<Feature>();
        assignFeaturesGrid.addColumn(Feature::name, "name").setHeader("Name");
        assignFeaturesGrid.addItemClickListener(event -> selectedFeature.set(event.getItem()));

        Button featureAssignButton = new Button();
        featureAssignButton.setText("Assign");

        Button featureAssignCancelButton = new Button();
        featureAssignCancelButton.setText("Cancel");

        Label assignFeatureError = new Label();
        assignFeatureError.getStyle().set("color", "red");
        assignFeatureError.setVisible(false);

        Dialog assignDialog = new Dialog();
        assignDialog.setCloseOnEsc(true);
        assignDialog.setCloseOnOutsideClick(true);
        assignDialog.setHeaderTitle("Assign feature");
        assignDialog.setWidth("50%");
        assignDialog.addAttachListener(event -> {
            assignDialog.setHeaderTitle("Assign feature for " + selectedUser.get().username());
            List<Feature> assignableFeatures = featureService.list();
            assignableFeatures.removeAll(selectedUser.get().features());

            assignFeaturesGrid.setItems(assignableFeatures);
        });
        assignDialog.addDetachListener(event -> {
            assignFeatureError.setVisible(false);
            selectedUser.set(null);
            selectedFeature.set(null);
        });

        HorizontalLayout assignDialogButtonsLayout = new HorizontalLayout(featureAssignCancelButton, featureAssignButton);
        assignDialogButtonsLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        assignDialog.add(assignFeaturesGrid);
        assignDialog.add(assignDialogButtonsLayout);
        assignDialog.add(assignFeatureError);

        featureAssignCancelButton.addClickListener(event -> {
            selectedUser.set(null);
            selectedFeature.set(null);
            assignDialog.close();
        });

        var revokeFeaturesGrid = new Grid<Feature>();
        revokeFeaturesGrid.addColumn(Feature::name, "name").setHeader("Name");
        revokeFeaturesGrid.addItemClickListener(event -> {
            selectedFeature.set(event.getItem());
        });

        Button featureRevokeButton = new Button();
        featureRevokeButton.setText("Revoke");

        Button featureRevokeCancelButton = new Button();
        featureRevokeCancelButton.setText("Cancel");

        Label revokeFeatureError = new Label();
        revokeFeatureError.getStyle().set("color", "red");
        revokeFeatureError.setVisible(false);

        Dialog revokeDialog = new Dialog();
        revokeDialog.setCloseOnEsc(true);
        revokeDialog.setCloseOnOutsideClick(true);
        revokeDialog.setHeaderTitle("Revoke feature");
        revokeDialog.setWidth("50%");
        revokeDialog.addAttachListener(event -> {
            revokeDialog.setHeaderTitle("Revoke feature for user " + selectedUser.get().username());
            revokeFeaturesGrid.setItems(selectedUser.get().features());
        });
        revokeDialog.addDetachListener(event -> {
            revokeFeatureError.setVisible(false);
            selectedUser.set(null);
            selectedFeature.set(null);
        });

        HorizontalLayout revokeDialogButtonsLayout = new HorizontalLayout(featureRevokeCancelButton, featureRevokeButton);
        revokeDialogButtonsLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        revokeDialog.add(revokeFeaturesGrid);
        revokeDialog.add(revokeDialogButtonsLayout);
        revokeDialog.add(revokeFeatureError);

        featureRevokeCancelButton.addClickListener(event -> {
            selectedUser.set(null);
            selectedFeature.set(null);
            revokeDialog.close();
        });

        if(securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Log out", e -> securityService.logout());

            HorizontalLayout header = new HorizontalLayout(logo, logout);

            header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            header.expand(logo);
            header.setWidth("100%");
            header.addClassNames("py-0", "px-m");

            VerticalLayout mainVerticalLayout = new VerticalLayout();
            mainVerticalLayout.setWidth("50%");
            var featureGrid = new Grid<Feature>();
            featureGrid.addColumn(Feature::name, "name").setHeader("Name");
            featureGrid.setItems(featureService.list());

            Input featureNameInput = new Input();
            featureNameInput.setPlaceholder("Feature name...");
            featureNameInput.setRequiredIndicatorVisible(true);

            Label featureErrorLabel = new Label();
            featureErrorLabel.getStyle().set("color", "red");
            featureErrorLabel.setVisible(false);

            Button addFeatureButton = new Button();
            addFeatureButton.setText("Add feature");
            addFeatureButton.addClickListener(event -> {
                featureErrorLabel.setVisible(false);
               String featureName = featureNameInput.getValue();
               if(featureName != null && !featureName.isEmpty()) {
                   if(featureService.featureExists(featureName)) {
                       featureErrorLabel.setText(String.format("Feature with name %s already exists", featureName));
                       featureErrorLabel.setVisible(true);
                   } else {
                       featureService.saveFeature(featureName);  // Ignoring return type
                       featureGrid.setItems(featureService.list());
                   }
               } else {
                    featureErrorLabel.setText("Feature name is required");
                    featureErrorLabel.setVisible(true);
               }
            });

            mainVerticalLayout.add(new HorizontalLayout(featureNameInput, addFeatureButton), featureGrid, featureErrorLabel);

            VerticalLayout verticalLayout2 = new VerticalLayout();
            verticalLayout2.setWidth("50%");
            var userGrid = new Grid<User>();
            userGrid.addColumn(User::username, "username").setHeader("Username");
            userGrid.addColumn(user -> user.features().stream().map(Feature::name).collect(Collectors.joining(", ")), "features").setHeader("Accessible features");
            userGrid.addColumn(new NativeButtonRenderer<>("Assign", user -> {
                selectedUser.set(user);
                assignDialog.open();
            })).setHeader("Assign");
            userGrid.addColumn(new NativeButtonRenderer<>("Revoke", user -> {
                selectedUser.set(user);
                revokeDialog.open();
            })).setHeader("Revoke");
            userGrid.setItems(userService.list());
            featureAssignButton.addClickListener(event -> {
                if(assignFeaturesGrid.getSelectedItems().isEmpty()) {
                    assignFeatureError.setText("Please select a feature to assign");
                    assignFeatureError.setVisible(true);
                } else {
                    featureService.assignFeature(selectedUser.get().username(), selectedFeature.get().name());
                    userGrid.setItems(userService.list());
                    assignDialog.close();
                }
            });
            assignFeaturesGrid.addItemDoubleClickListener(event -> {
                featureService.assignFeature(selectedUser.get().username(), event.getItem().name());
                userGrid.setItems(userService.list());
                assignDialog.close();
            });
            featureRevokeButton.addClickListener(event -> {
                if(revokeFeaturesGrid.getSelectedItems().isEmpty()) {
                    revokeFeatureError.setText("Please select a feature to revoke");
                    revokeFeatureError.setVisible(true);
                } else {
                    featureService.revokeFeature(selectedUser.get().username(), selectedFeature.get().name());
                    userGrid.setItems(userService.list());
                    revokeDialog.close();
                }
            });
            revokeFeaturesGrid.addItemDoubleClickListener(event -> {
                featureService.revokeFeature(selectedUser.get().username(), selectedFeature.get().name());
                userGrid.setItems(userService.list());
                revokeDialog.close();
            });

            Input usernameInput = new Input();
            usernameInput.setPlaceholder("Username...");
            usernameInput.setRequiredIndicatorVisible(true);
            usernameInput.getElement().setAttribute("autocomplete", "off");

            Input passwordInput = new Input();
            passwordInput.setPlaceholder("Password...");
            passwordInput.setType("password");
            passwordInput.setRequiredIndicatorVisible(true);
            passwordInput.getElement().setAttribute("autocomplete", "off");

            Label userErrorLabel = new Label();
            userErrorLabel.getStyle().set("color", "red");
            userErrorLabel.setVisible(false);

            Button addUserButton = new Button();
            addUserButton.setText("Add User");
            addUserButton.addClickListener(event -> {
                userErrorLabel.setVisible(false);

                String username = usernameInput.getValue();
                String password = passwordInput.getValue();

                boolean validUsername = username != null && !username.isEmpty();
                boolean validPassword = password != null && !password.isEmpty();

                if(validUsername && validPassword) {
                    if(userService.userExists(username)) {
                        userErrorLabel.setText(String.format("Username (%s) already exists", username));
                        userErrorLabel.setVisible(true);
                    } else {
                        userService.createUser(username, password); // Ignoring return type
                        userGrid.setItems(userService.list());
                    }
                } else {
                    if(!validUsername) {
                        userErrorLabel.setText("Username is required");
                        userErrorLabel.setVisible(true);
                    } else {
                        userErrorLabel.setText("Password is required");
                        userErrorLabel.setVisible(true);
                    }
                }
            });

            verticalLayout2.add(new HorizontalLayout(usernameInput, passwordInput, addUserButton), userGrid, userErrorLabel);

            var grids = new HorizontalLayout(mainVerticalLayout, verticalLayout2);
            grids.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            grids.expand(logo);
            grids.setWidth("100%");
            grids.addClassNames("py-0", "px-m");

            add(header, grids);
        }
    }
}