
package com.unnsvc.erhena.wizards;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.bidi.StructuredTextTypeHandlerFactory;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.dialogs.WorkingSetConfigurationBlock;

public class RhenaModuleWizardPage extends WizardPage {

	private final NameGroup fNameGroup;
	private final LocationGroup fLocationGroup;
	private final Validator fValidator;
	private final WorkingSetGroup fWorkingSetGroup;

	public RhenaModuleWizardPage() {
		super("rhenaWizard");
		setPageComplete(false);
		setTitle("New Rhena Module");
		setDescription("This wizard creates a new Rhena module project");

		fNameGroup = new NameGroup();
		fLocationGroup = new LocationGroup();
		fWorkingSetGroup = new WorkingSetGroup();

		// establish connections
		fNameGroup.addObserver(fLocationGroup);

		// initialize all elements
		fNameGroup.notifyObservers();

		// create and connect validator
		fValidator = new Validator();
		fNameGroup.addObserver(fValidator);
		fLocationGroup.addObserver(fValidator);

		// initialize defaults
		setProjectName(""); //$NON-NLS-1$
		setProjectLocationURI(null);
		setWorkingSets(new IWorkingSet[0]);
	}

	public void init(IStructuredSelection selection, IWorkbenchPart activePart) {

		setWorkingSets(getSelectedWorkingSet(selection, activePart));
	}

	public void setWorkingSets(IWorkingSet[] workingSets) {

		if (workingSets == null) {
			throw new IllegalArgumentException();
		}
		fWorkingSetGroup.setWorkingSets(workingSets);
	}

	@Override
	public void setVisible(boolean visible) {

		super.setVisible(visible);
		if (visible) {
			fNameGroup.postSetFocus();
		}
	}

	public IWorkingSet[] getWorkingSets() {

		return fWorkingSetGroup.getSelectedWorkingSets();
	}

	private static final IWorkingSet[] EMPTY_WORKING_SET_ARRAY = new IWorkingSet[0];

	private IWorkingSet[] getSelectedWorkingSet(IStructuredSelection selection, IWorkbenchPart activePart) {

		IWorkingSet[] selected = getSelectedWorkingSet(selection);
		if (selected != null && selected.length > 0) {
			for (int i = 0; i < selected.length; i++) {
				if (!isValidWorkingSet(selected[i]))
					return EMPTY_WORKING_SET_ARRAY;
			}
			return selected;
		}

		if (!(activePart instanceof PackageExplorerPart))
			return EMPTY_WORKING_SET_ARRAY;

		PackageExplorerPart explorerPart = (PackageExplorerPart) activePart;
		if (explorerPart.getRootMode() == PackageExplorerPart.PROJECTS_AS_ROOTS) {
			// Get active filter
			IWorkingSet filterWorkingSet = explorerPart.getFilterWorkingSet();
			if (filterWorkingSet == null)
				return EMPTY_WORKING_SET_ARRAY;

			if (!isValidWorkingSet(filterWorkingSet))
				return EMPTY_WORKING_SET_ARRAY;

			return new IWorkingSet[] { filterWorkingSet };
		} else {
			// If we have been gone into a working set return the working set
			Object input = explorerPart.getViewPartInput();
			if (!(input instanceof IWorkingSet))
				return EMPTY_WORKING_SET_ARRAY;

			IWorkingSet workingSet = (IWorkingSet) input;
			if (!isValidWorkingSet(workingSet))
				return EMPTY_WORKING_SET_ARRAY;

			return new IWorkingSet[] { workingSet };
		}
	}

	private IWorkingSet[] getSelectedWorkingSet(IStructuredSelection selection) {

		if (!(selection instanceof ITreeSelection))
			return EMPTY_WORKING_SET_ARRAY;

		ITreeSelection treeSelection = (ITreeSelection) selection;
		if (treeSelection.isEmpty())
			return EMPTY_WORKING_SET_ARRAY;

		List<?> elements = treeSelection.toList();
		if (elements.size() == 1) {
			Object element = elements.get(0);
			TreePath[] paths = treeSelection.getPathsFor(element);
			if (paths.length != 1)
				return EMPTY_WORKING_SET_ARRAY;

			TreePath path = paths[0];
			if (path.getSegmentCount() == 0)
				return EMPTY_WORKING_SET_ARRAY;

			Object candidate = path.getSegment(0);
			if (!(candidate instanceof IWorkingSet))
				return EMPTY_WORKING_SET_ARRAY;

			IWorkingSet workingSetCandidate = (IWorkingSet) candidate;
			if (isValidWorkingSet(workingSetCandidate))
				return new IWorkingSet[] { workingSetCandidate };

			return EMPTY_WORKING_SET_ARRAY;
		}

		ArrayList<IWorkingSet> result = new ArrayList<>();
		for (Iterator<?> iterator = elements.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			if (element instanceof IWorkingSet && isValidWorkingSet((IWorkingSet) element)) {
				result.add((IWorkingSet) element);
			}
		}
		return result.toArray(new IWorkingSet[result.size()]);
	}

	public URI getProjectLocationURI() {

		if (fLocationGroup.isUseDefaultSelected()) {
			return null;
		}
		return URIUtil.toURI(fLocationGroup.getLocation());
	}

	private static boolean isValidWorkingSet(IWorkingSet workingSet) {

		String id = workingSet.getId();
		if (!IWorkingSetIDs.JAVA.equals(id) && !IWorkingSetIDs.RESOURCE.equals(id))
			return false;

		if (workingSet.isAggregateWorkingSet())
			return false;

		return true;
	}

	@Override
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// create UI elements
		Control nameControl = createNameControl(composite);
		nameControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Control locationControl = createLocationControl(composite);
		locationControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Control workingSetControl = createWorkingSetControl(composite);
		workingSetControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		setControl(composite);
	}

	private GridLayout initGridLayout(GridLayout layout, boolean margins) {

		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth = 0;
			layout.marginHeight = 0;
		}
		return layout;
	}

	protected Control createNameControl(Composite composite) {

		return fNameGroup.createControl(composite);
	}

	protected Control createLocationControl(Composite composite) {

		return fLocationGroup.createControl(composite);
	}

	protected Control createWorkingSetControl(Composite composite) {

		return fWorkingSetGroup.createControl(composite);
	}

	public String getGroupName() {

		return fNameGroup.getGroup();
	}

	public String getProjectName() {

		return fNameGroup.getName();
	}

	public void setProjectName(String name) {

		if (name == null)
			throw new IllegalArgumentException();

		fNameGroup.setName(name);
	}

	@Override
	protected void setControl(Control newControl) {

		Dialog.applyDialogFont(newControl);

		// PlatformUI.getWorkbench().getHelpSystem().setHelp(newControl, "...");

		super.setControl(newControl);
	}

	// public URI getProjectLocationURI() {
	// if (fLocationGroup.isUseDefaultSelected()) {
	// return null;
	// }
	// return URIUtil.toURI(fLocationGroup.getLocation());
	// }

	public void setProjectLocationURI(URI uri) {

		IPath path = uri != null ? URIUtil.toPath(uri) : null;

		fLocationGroup.setLocation(path);
	}

	private class NameGroup extends Observable implements IDialogFieldListener {

		private StringDialogField fGroupField;
		private StringDialogField fNameField;

		public NameGroup() {
			// text field for project name

			fGroupField = new StringDialogField();
			fGroupField.setLabelText("Component name");
			fGroupField.setDialogFieldListener(this);

			fNameField = new StringDialogField();
			fNameField.setLabelText(NewWizardMessages.NewJavaProjectWizardPageOne_NameGroup_label_text);
			fNameField.setDialogFieldListener(this);
		}

		public Control createControl(Composite composite) {

			Composite nameComposite = new Composite(composite, SWT.NONE);
			nameComposite.setFont(composite.getFont());
			nameComposite.setLayout(new GridLayout(2, false));

			fGroupField.doFillIntoGrid(nameComposite, 2);
			LayoutUtil.setHorizontalGrabbing(fGroupField.getTextControl(null));

			fNameField.doFillIntoGrid(nameComposite, 2);
			LayoutUtil.setHorizontalGrabbing(fNameField.getTextControl(null));

			return nameComposite;
		}

		protected void fireEvent() {

			setChanged();
			notifyObservers();
		}

		public String getName() {

			return fNameField.getText().trim();
		}

		public String getGroup() {

			return fGroupField.getText().trim();
		}

		public void postSetFocus() {

			fNameField.postSetFocusOnDialogField(getShell().getDisplay());
		}

		public void setName(String name) {

			fNameField.setText(name);
		}

		public void setGroup(String group) {

			fGroupField.setText(group);
		}

		@Override
		public void dialogFieldChanged(DialogField field) {

			fireEvent();
		}
	}

	private class LocationGroup extends Observable implements Observer, IStringButtonAdapter, IDialogFieldListener {

		protected final SelectionButtonDialogField fUseDefaults;
		protected final StringButtonDialogField fLocation;

		private String fPreviousExternalLocation;

		private static final String DIALOGSTORE_LAST_EXTERNAL_LOC = JavaUI.ID_PLUGIN + ".last.external.project"; //$NON-NLS-1$

		public LocationGroup() {
			fUseDefaults = new SelectionButtonDialogField(SWT.CHECK);
			fUseDefaults.setDialogFieldListener(this);
			fUseDefaults.setLabelText(NewWizardMessages.NewJavaProjectWizardPageOne_LocationGroup_location_desc);

			fLocation = new StringButtonDialogField(this);
			fLocation.setDialogFieldListener(this);
			fLocation.setLabelText(NewWizardMessages.NewJavaProjectWizardPageOne_LocationGroup_locationLabel_desc);
			fLocation.setButtonLabel(NewWizardMessages.NewJavaProjectWizardPageOne_LocationGroup_browseButton_desc);

			fUseDefaults.setSelection(true);

			fPreviousExternalLocation = ""; //$NON-NLS-1$
		}

		public Control createControl(Composite composite) {

			final int numColumns = 4;

			final Composite locationComposite = new Composite(composite, SWT.NONE);
			locationComposite.setLayout(new GridLayout(numColumns, false));

			fUseDefaults.doFillIntoGrid(locationComposite, numColumns);
			fLocation.doFillIntoGrid(locationComposite, numColumns);
			LayoutUtil.setHorizontalGrabbing(fLocation.getTextControl(null));
			BidiUtils.applyBidiProcessing(fLocation.getTextControl(null), StructuredTextTypeHandlerFactory.FILE);

			return locationComposite;
		}

		protected void fireEvent() {

			setChanged();
			notifyObservers();
		}

		protected String getDefaultPath(String name) {

			final IPath path = Platform.getLocation().append(name);
			return path.toOSString();
		}

		@Override
		public void update(Observable o, Object arg) {

			if (isUseDefaultSelected()) {
				fLocation.setText(getDefaultPath(fNameGroup.getName()));
			}
			fireEvent();
		}

		public IPath getLocation() {

			if (isUseDefaultSelected()) {
				return Platform.getLocation();
			}
			return Path.fromOSString(fLocation.getText().trim());
		}

		public boolean isUseDefaultSelected() {

			return fUseDefaults.isSelected();
		}

		public void setLocation(IPath path) {

			fUseDefaults.setSelection(path == null);
			if (path != null) {
				fLocation.setText(path.toOSString());
			} else {
				fLocation.setText(getDefaultPath(fNameGroup.getName()));
			}
			fireEvent();
		}

		@Override
		public void changeControlPressed(DialogField field) {

			final DirectoryDialog dialog = new DirectoryDialog(getShell());
			dialog.setMessage(NewWizardMessages.NewJavaProjectWizardPageOne_directory_message);
			String directoryName = fLocation.getText().trim();
			if (directoryName.length() == 0) {
				String prevLocation = JavaPlugin.getDefault().getDialogSettings().get(DIALOGSTORE_LAST_EXTERNAL_LOC);
				if (prevLocation != null) {
					directoryName = prevLocation;
				}
			}

			if (directoryName.length() > 0) {
				final File path = new File(directoryName);
				if (path.exists())
					dialog.setFilterPath(directoryName);
			}
			final String selectedDirectory = dialog.open();
			if (selectedDirectory != null) {
				String oldDirectory = new Path(fLocation.getText().trim()).lastSegment();
				fLocation.setText(selectedDirectory);
				String lastSegment = new Path(selectedDirectory).lastSegment();
				if (lastSegment != null && (fNameGroup.getName().length() == 0 || fNameGroup.getName().equals(oldDirectory))) {
					fNameGroup.setName(lastSegment);
				}
				JavaPlugin.getDefault().getDialogSettings().put(DIALOGSTORE_LAST_EXTERNAL_LOC, selectedDirectory);
			}
		}

		@Override
		public void dialogFieldChanged(DialogField field) {

			if (field == fUseDefaults) {
				final boolean checked = fUseDefaults.isSelected();
				if (checked) {
					fPreviousExternalLocation = fLocation.getText();
					fLocation.setText(getDefaultPath(fNameGroup.getName()));
					fLocation.setEnabled(false);
				} else {
					fLocation.setText(fPreviousExternalLocation);
					fLocation.setEnabled(true);
				}
			}
			fireEvent();
		}
	}

	private class WorkingSetGroup {

		private WorkingSetConfigurationBlock fWorkingSetBlock;

		public WorkingSetGroup() {
			String[] workingSetIds = new String[] { IWorkingSetIDs.JAVA, IWorkingSetIDs.RESOURCE };
			fWorkingSetBlock = new WorkingSetConfigurationBlock(workingSetIds, JavaPlugin.getDefault().getDialogSettings());
			// fWorkingSetBlock.setDialogMessage(NewWizardMessages.NewJavaProjectWizardPageOne_WorkingSetSelection_message);
		}

		public Control createControl(Composite composite) {

			Group workingSetGroup = new Group(composite, SWT.NONE);
			workingSetGroup.setFont(composite.getFont());
			workingSetGroup.setText(NewWizardMessages.NewJavaProjectWizardPageOne_WorkingSets_group);
			workingSetGroup.setLayout(new GridLayout(1, false));

			fWorkingSetBlock.createContent(workingSetGroup);

			return workingSetGroup;
		}

		public void setWorkingSets(IWorkingSet[] workingSets) {

			fWorkingSetBlock.setWorkingSets(workingSets);
		}

		public IWorkingSet[] getSelectedWorkingSets() {

			return fWorkingSetBlock.getSelectedWorkingSets();
		}
	}

	private class Validator implements Observer {

		@Override
		public void update(Observable o, Object arg) {

			final IWorkspace workspace = JavaPlugin.getWorkspace();

			final String name = fNameGroup.getName();

			// check whether the project name field is empty
			if (name.length() == 0) {
				setErrorMessage(null);
				setMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_enterProjectName);
				setPageComplete(false);
				return;
			}

			// check whether the project name is valid
			final IStatus nameStatus = workspace.validateName(name, IResource.PROJECT);
			if (!nameStatus.isOK()) {
				setErrorMessage(nameStatus.getMessage());
				setPageComplete(false);
				return;
			}

			// check whether project already exists
			final IProject handle = workspace.getRoot().getProject(name);
			if (handle.exists()) {
				setErrorMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_projectAlreadyExists);
				setPageComplete(false);
				return;
			}

			IPath projectLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(name);
			if (projectLocation.toFile().exists()) {
				try {
					// correct casing
					String canonicalPath = projectLocation.toFile().getCanonicalPath();
					projectLocation = new Path(canonicalPath);
				} catch (IOException e) {
					JavaPlugin.log(e);
				}

				String existingName = projectLocation.lastSegment();
				if (!existingName.equals(fNameGroup.getName())) {
					setErrorMessage(Messages.format(NewWizardMessages.NewJavaProjectWizardPageOne_Message_invalidProjectNameForWorkspaceRoot,
							BasicElementLabels.getResourceName(existingName)));
					setPageComplete(false);
					return;
				}

			}

			final String location = fLocationGroup.getLocation().toOSString();

			// check whether location is empty
			if (location.length() == 0) {
				setErrorMessage(null);
				setMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_enterLocation);
				setPageComplete(false);
				return;
			}

			// check whether the location is a syntactically correct path
			if (!Path.EMPTY.isValidPath(location)) {
				setErrorMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_invalidDirectory);
				setPageComplete(false);
				return;
			}

			IPath projectPath = null;
			if (!fLocationGroup.isUseDefaultSelected()) {
				projectPath = Path.fromOSString(location);
				if (!projectPath.toFile().exists()) {
					// check non-existing external location
					if (!canCreate(projectPath.toFile())) {
						setErrorMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_cannotCreateAtExternalLocation);
						setPageComplete(false);
						return;
					}
				}
			}

			// validate the location
			final IStatus locationStatus = workspace.validateProjectLocation(handle, projectPath);
			if (!locationStatus.isOK()) {
				setErrorMessage(locationStatus.getMessage());
				setPageComplete(false);
				return;
			}

			setPageComplete(true);

			setErrorMessage(null);
			setMessage(null);
		}

		private boolean canCreate(File file) {

			while (!file.exists()) {
				file = file.getParentFile();
				if (file == null)
					return false;
			}

			return file.canWrite();
		}
	}
}
