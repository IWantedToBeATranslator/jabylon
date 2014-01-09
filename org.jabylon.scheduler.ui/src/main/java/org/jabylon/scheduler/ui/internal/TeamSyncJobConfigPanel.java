/**
 * (C) Copyright 2013 Jabylon (http://www.jabylon.org) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 *
 */
package org.jabylon.scheduler.ui.internal;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.jabylon.common.util.PreferencesUtil;
import org.jabylon.properties.Project;
import org.jabylon.properties.ProjectVersion;
import org.jabylon.rest.ui.model.AttachableModel;
import org.jabylon.rest.ui.model.BooleanPreferencesPropertyModel;
import org.jabylon.rest.ui.model.PreferencesPropertyModel;
import org.jabylon.rest.ui.wicket.BasicPanel;
import org.jabylon.rest.ui.wicket.components.ControlGroup;
import org.jabylon.rest.ui.wicket.config.AbstractConfigSection;
import org.jabylon.scheduler.JobExecution;
import org.jabylon.scheduler.internal.jobs.TeamCommitJob;
import org.jabylon.scheduler.internal.jobs.TeamUpdateJob;
import org.jabylon.security.CommonPermissions;
import org.osgi.service.prefs.Preferences;

/**
 * @author Johannes Utzig (jutzig.dev@googlemail.com)
 *
 */
public class TeamSyncJobConfigPanel extends BasicPanel<ProjectVersion> {

	private static final long serialVersionUID = 1L;

	public TeamSyncJobConfigPanel(String id, IModel<ProjectVersion> model, Preferences root) {
		super(id, model);
		Preferences updateConfig = PreferencesUtil.getNodeForJob(root, TeamUpdateJob.JOB_ID);
		PreferencesPropertyModel updateModel = new PreferencesPropertyModel(updateConfig, JobExecution.PROP_JOB_SCHEDULE, "");
		ControlGroup updateCronGroup = new ControlGroup("update-cron-group", nls("update.cron.label"), nls("update.cron.description"));
		TextField<String> updateCron = new TextField<String>("update-cron", updateModel);
		updateCron.setConvertEmptyInputStringToNull(true);
		updateCronGroup.add(updateCron);
		add(updateCronGroup);

		BooleanPreferencesPropertyModel updateEnabledModel = new BooleanPreferencesPropertyModel(updateConfig, JobExecution.PROP_JOB_ACTIVE, false);
		ControlGroup updateEnabledCronGroup = new ControlGroup("update-cron-enabled-group", Model.of(""),nls("update.cron.enabled.description"));
		CheckBox updateEnabledCron = new CheckBox("update-cron-enabled", updateEnabledModel);
		updateEnabledCronGroup.add(updateEnabledCron);
		add(updateEnabledCronGroup);
		
		Preferences commitConfig = PreferencesUtil.getNodeForJob(root, TeamCommitJob.JOB_ID);
		PreferencesPropertyModel commitModel = new PreferencesPropertyModel(commitConfig, JobExecution.PROP_JOB_SCHEDULE, "");
		ControlGroup commitCronGroup = new ControlGroup("commit-cron-group", nls("commit.cron.label"), nls("commit.cron.description"));
		TextField<String> commitCron = new TextField<String>("commit-cron", commitModel);
		commitCron.setConvertEmptyInputStringToNull(true);
		commitCronGroup.add(commitCron);
		add(commitCronGroup);

		BooleanPreferencesPropertyModel commitEnabledModel = new BooleanPreferencesPropertyModel(commitConfig, JobExecution.PROP_JOB_ACTIVE, false);
		ControlGroup commitEnabledCronGroup = new ControlGroup("commit-cron-enabled-group", Model.of(""),nls("commit.cron.enabled.description"));
		CheckBox commitEnabledCron = new CheckBox("commit-cron-enabled", commitEnabledModel);
		commitEnabledCronGroup.add(commitEnabledCron);
		add(commitEnabledCronGroup);

	}

	public static class TeamSyncJobConfigSection extends AbstractConfigSection<ProjectVersion>{

		private static final long serialVersionUID = 1L;


		@Override
		public WebMarkupContainer doCreateContents(String id, IModel<ProjectVersion> input, Preferences config) {
			return new TeamSyncJobConfigPanel(id, input, config);
		}

		@Override
		public void commit(IModel<ProjectVersion> input, Preferences config) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getRequiredPermission() {
			
			String projectName = null;
			if(getDomainObject()!=null) {
				Project project = getParent(getModel());
				if(project!=null)
				 projectName = project.getName();
			}
			return CommonPermissions.constructPermission(CommonPermissions.PROJECT,projectName,CommonPermissions.ACTION_CONFIG);
		}

		private Project getParent(IModel<ProjectVersion> domainObject) {
			if(domainObject.getObject().getParent()!=null)
				return domainObject.getObject().getParent();
			if (domainObject instanceof AttachableModel) {
				AttachableModel<ProjectVersion> model = (AttachableModel<ProjectVersion>) domainObject;
				return (Project) model.getParent().getObject();
			}
			return null;
		}
		
		@Override
		public boolean isVisible(IModel<ProjectVersion> input, Preferences config) {
			String teamProvider = getParent(input).getTeamProvider();
			return super.isVisible(input, config) && teamProvider!=null;
		}
		
	}
	

	

}
