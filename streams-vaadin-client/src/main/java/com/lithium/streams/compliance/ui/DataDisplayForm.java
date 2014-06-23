package com.lithium.streams.compliance.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CustomComponent;

public class DataDisplayForm extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private JsonDisplau jsonDisplau_1;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public DataDisplayForm() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// jsonDisplau_1
		jsonDisplau_1 = new JsonDisplau();
		jsonDisplau_1.setImmediate(false);
		jsonDisplau_1.setWidth("580px");
		jsonDisplau_1.setHeight("100.0%");
		mainLayout.addComponent(jsonDisplau_1, "top:80.0px;bottom:73.0px;left:60.0px;");
		
		return mainLayout;
	}

}
