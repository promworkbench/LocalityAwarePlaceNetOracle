package org.processmining.localityawareplacenetoracle;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers.PlaceSetVisualizer;

public class PlaceNetVisualizer {

    @Plugin(name = "Visualize of Place Net", returnLabels = { "Place Graph" }, returnTypes = { JComponent.class }, parameterLabels = { "PlaceSet" }, userAccessible = true)
    @Visualizer
    public JComponent runUI(UIPluginContext context, PlaceSet output) {
    		PlaceSetVisualizer Visual = new PlaceSetVisualizer();    				
            return Visual.visualize(context, output);
    }
}