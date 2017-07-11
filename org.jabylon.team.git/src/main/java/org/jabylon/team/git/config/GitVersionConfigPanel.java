/**
 * (C) Copyright 2013 Jabylon (http://www.jabylon.org) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jabylon.team.git.config;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.eclipse.jgit.transport.RefSpec;
import org.jabylon.properties.Project;
import org.jabylon.properties.ProjectVersion;
import org.jabylon.rest.ui.model.AttachableModel;
import org.jabylon.rest.ui.model.BooleanPreferencesPropertyModel;
import org.jabylon.rest.ui.model.PreferencesPropertyModel;
import org.jabylon.rest.ui.wicket.BasicPanel;
import org.jabylon.rest.ui.wicket.components.ControlGroup;
import org.jabylon.rest.ui.wicket.config.AbstractConfigSection;
import org.jabylon.security.CommonPermissions;
import org.jabylon.team.git.GitConstants;
import org.osgi.service.prefs.Preferences;

public class GitVersionConfigPanel extends BasicPanel<ProjectVersion> {

    private static final long serialVersionUID = 1L;

    public GitVersionConfigPanel(String id, IModel<ProjectVersion> model, Preferences config) {
        super(id, model);

        PreferencesPropertyModel branchName = new PreferencesPropertyModel(config, GitConstants.KEY_BRANCH_NAME, (String)null);
        ControlGroup branchNameGroup = new ControlGroup("branch-name-group", nls("branch.name.label"),nls("branch.name.help"));
        TextField<String> branchNameTextField = new TextField<String>("branch-name",branchName);
        branchNameTextField.setRequired(false);
        branchNameTextField.setConvertEmptyInputStringToNull(true);
        branchNameGroup.add(branchNameTextField);
        add(branchNameGroup);

        PreferencesPropertyModel pushRefspecModel = new PreferencesPropertyModel(config, GitConstants.KEY_PUSH_REFSPEC, "");
        ControlGroup pushRefspecGroup = new ControlGroup("push-refspec-group", nls("push.refspec.label"),nls("push.refspec.help"));
        TextField<String> refspecTextField = new TextField<String>("push-refspec",pushRefspecModel);
        refspecTextField.add(new RefspecValidator());
        refspecTextField.setRequired(false);
        refspecTextField.setConvertEmptyInputStringToNull(false);
        pushRefspecGroup.add(refspecTextField);
        add(pushRefspecGroup);
        
        
        BooleanPreferencesPropertyModel rebase = new BooleanPreferencesPropertyModel(config, GitConstants.KEY_REBASE, true);
        ControlGroup rebaseGroup = new ControlGroup("rebase-group", nls("rebase.label"),nls("rebase.help"));
        CheckBox rebaseCheckbox = new CheckBox("rebase",rebase);
        rebaseGroup.add(rebaseCheckbox);
        add(rebaseGroup);
    }

    public static class GitVersionConfigSection extends AbstractConfigSection<ProjectVersion>{

        private static final long serialVersionUID = 1L;

        private boolean gitSelected(IModel<ProjectVersion> model) {
            return "Git".equals(getProject(model).getTeamProvider());
        }

        Project getProject(IModel<ProjectVersion> model) {
            if (model instanceof AttachableModel) {
                AttachableModel<ProjectVersion> m = (AttachableModel<ProjectVersion>) model;
                return (Project) m.getParent().getObject();
            }
            return model.getObject().getParent();
        }


        @Override
        public WebMarkupContainer doCreateContents(String id, IModel<ProjectVersion> input, Preferences config) {
            GitVersionConfigPanel panel = new GitVersionConfigPanel(id, input, config);
            panel.setVisible(gitSelected(input));
            return panel;
        }

        @Override
        public boolean isVisible(IModel<ProjectVersion> input, Preferences config) {
            return gitSelected(input) && super.isVisible(input, config);
        }


        @Override
        public void commit(IModel<ProjectVersion> input, Preferences config) {
            // nothing to do

        }

        @Override
        public String getRequiredPermission() {
            Project project = getProject(getModel());
            String projectName = null;
            if(project!=null)
                projectName = project.getName();
            return CommonPermissions.constructPermission(CommonPermissions.PROJECT,projectName,CommonPermissions.ACTION_CONFIG);
        }

    }

    private static class RefspecValidator implements IValidator<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> validatable) {
            String value = validatable.getValue();
            try {
                new RefSpec(value);
            } catch (Exception e) {
                validatable.error(new ValidationError(e.getMessage()));
            }

        }

    }

}
