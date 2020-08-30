package org.freeplane.plugin.script.proxy;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Map.Entry;

import org.freeplane.api.NodeChangeListener;
import org.freeplane.api.NodeCondition;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.features.filter.Filter;
import org.freeplane.features.filter.FilterController;
import org.freeplane.features.filter.condition.ICondition;
import org.freeplane.features.map.MapController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.styles.MapStyle;
import org.freeplane.features.styles.MapStyleModel;
import org.freeplane.features.ui.IMapViewManager;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;
import org.freeplane.plugin.script.FormulaUtils;
import org.freeplane.plugin.script.ScriptContext;
import org.freeplane.plugin.script.proxy.Proxy.Map;
import org.freeplane.plugin.script.proxy.Proxy.MindMap;
import org.freeplane.plugin.script.proxy.Proxy.Node;

import groovy.lang.Closure;

public class MapProxy extends AbstractProxy<MapModel> implements MindMap, Map {
	public MapProxy(final MapModel map, final ScriptContext scriptContext) {
		super(map, scriptContext);
	}

	// MapRO: R
	@Override
	public Node node(final String id) {
		final NodeModel node = getDelegate().getNodeForID(id);
		return node != null ? new NodeProxy(node, getScriptContext()) : null;
	}

	// MapRO: R
	@Override
	public Node getRoot() {
		final NodeModel rootNode = getDelegate().getRootNode();
		return new NodeProxy(rootNode, getScriptContext());
	}

	@Override
	@Deprecated
	public Node getRootNode() {
		return getRoot();
	}

	// MapRO: R
	@Override
	public File getFile() {
		return getDelegate().getFile();
	}

	// MapRO: R
	@Override
	public String getName() {
		final IMapViewManager mapViewManager = getMapViewManager();
		for (Entry<String, MapModel> map : mapViewManager.getMaps().entrySet()) {
			if (map.getValue().equals(getDelegate()))
				return map.getKey();
		}
		return null;
	}

	// MapRO: R
	@Override
	public boolean isSaved() {
		return getDelegate().isSaved();
	}

    // MapRO: R
    @Override
	public Color getBackgroundColor() {
        // see MapBackgroundColorAction
        final MapStyle mapStyle = Controller.getCurrentModeController().getExtension(MapStyle.class);
        final MapStyleModel model = (MapStyleModel) mapStyle.getMapHook(getDelegate());
        if (model != null) {
            return model.getBackgroundColor();
        }
        else {
            final String colorPropertyString = ResourceController.getResourceController().getProperty(
                MapStyle.RESOURCES_BACKGROUND_COLOR);
            final Color defaultBgColor = ColorUtils.stringToColor(colorPropertyString);
            return defaultBgColor;
        }
    }

    // MapRO: R
    @Override
	public String getBackgroundColorCode() {
        return ColorUtils.colorToString(getBackgroundColor());
    }

	// Map: R/W
	@Override
	public boolean close(boolean force, boolean allowInteraction) {
		if (!getDelegate().isSaved() && !force && !allowInteraction)
			throw new RuntimeException("will not close an unsaved map without being told so");
		MMapController mapController = (MMapController) getModeController().getMapController();
		if(force && ! allowInteraction) {
            mapController.closeWithoutSaving(getDelegate());
            return true;
        } else if(allowInteraction)
		    return mapController.close(getDelegate());
		else
		    return false;
	}

	private boolean changeToThisMap(final IMapViewManager mapViewManager) {
		if (! GraphicsEnvironment.isHeadless()) {
			String mapKey = findMapViewKey(mapViewManager);
			if (mapKey != null) {
                mapViewManager.changeToMapView(mapKey);
                return true;
            }
		}
		return false;
	}

	private IMapViewManager getMapViewManager() {
		return getModeController().getController().getMapViewManager();
	}

	private String findMapViewKey(final IMapViewManager mapViewManager) {
		for (Entry<String, MapModel> entry : mapViewManager.getMaps().entrySet()) {
			if (entry.getValue().equals(getDelegate())) {
				return entry.getKey();
			}
		}
		return null;
	}

	// Map: R/W
	@Override
	public boolean save(boolean allowInteraction) {
	    if (!getDelegate().isSaved() && getDelegate().getURL() == null) {
	        if(! (allowInteraction  && changeToThisMap(getMapViewManager())))
                throw new RuntimeException("no url set for map " + getDelegate());
            
	    }
	    return ((MFileManager) UrlManager.getController()).save(getDelegate());
	}

	// Map: R/W
	@Override
	public boolean saveAs(File file) {
		return MFileManager.getController(getModeController()).save(getDelegate(), file);
	}

	// Map: R/W
	@Override
	public void setName(final String title) {
		if (changeToThisMap(getMapViewManager()))
		    Controller.getCurrentController().getMapViewManager().getMapViewComponent().setName(title);
	}

	// Map: R/W
	@Override
	public void setSaved(final boolean isSaved) {
		Controller.getCurrentModeController().getMapController().setSaved(getDelegate(), isSaved);
	}

    // Map: R/W
    @Override
	public void setBackgroundColor(Color color) {
        final MapStyle mapStyle = Controller.getCurrentModeController().getExtension(MapStyle.class);
        final MapStyleModel model = (MapStyleModel) mapStyle.getMapHook(getDelegate());
        mapStyle.setBackgroundColor(model, color);
    }

    // Map: R/W
    @Override
	public void setBackgroundColorCode(String rgbString) {
        setBackgroundColor(ColorUtils.stringToColor(rgbString));
    }

	// Map: R/W
	public void setFilter(final Closure<Boolean> closure) {
		setFilter(false, false, closure);
	}

	// Map: R/W
	public void filter(final Closure<Boolean> closure) {
		setFilter(closure);
	}


	// Map: R/W
	@Override
	public void filter(NodeCondition condition) {
		setFilter(condition);
	}

	// Map: R/W
	@Override
	public void setFilter(NodeCondition condition) {
		setFilter(false, false, condition);
	}

	// Map: R/W
	@Override
	public void filter(boolean showAncestors, boolean showDescendants, NodeCondition condition) {
		setFilter(showAncestors, showDescendants, condition);
	}


	// Map: R/W
	@Override
	public void setFilter(final boolean showAncestors, final boolean showDescendants, final NodeCondition nc) {
		final ICondition condition = ProxyUtils.createCondition(nc, getScriptContext());
		setFilter(false, showAncestors, showDescendants, condition);
	}

	// Map: R/W
	public void setFilter(final boolean showAncestors, final boolean showDescendants, final Closure<Boolean> closure) {
		final ICondition condition = ProxyUtils.createCondition(closure, getScriptContext());
		setFilter(false, showAncestors, showDescendants, condition);
	}

    // Map: R/W
    @Override
    public void hide(final boolean showAncestors, final boolean showDescendants, final NodeCondition nc) {
        final ICondition condition = ProxyUtils.createCondition(nc, getScriptContext());
        setFilter(true, showAncestors, showDescendants, condition);
    }

    // Map: R/W
    public void hide(final boolean showAncestors, final boolean showDescendants, final Closure<Boolean> closure) {
        final ICondition condition = ProxyUtils.createCondition(closure, getScriptContext());
        setFilter(true, showAncestors, showDescendants, condition);
    }
    
	private void setFilter(boolean hideMatches, final boolean showAncestors, final boolean showDescendants, final ICondition condition) {
		final FilterController filterController = FilterController.getCurrentFilterController();
		if (condition == null) {
			filterController.applyNoFiltering(getDelegate());
		}
		else {
			final Filter filter = new Filter(condition, hideMatches, showAncestors,
			    showDescendants, null);
			filterController.applyFilter(getDelegate(), true, filter);
		}
	}

	// Map: R/W
	public void filter(final boolean showAncestors, final boolean showDescendants, final Closure<Boolean> closure) {
		setFilter(showAncestors, showDescendants, closure);
	}

	// Map: R/W
	@Override
	public void redoFilter() {
		FilterController.getCurrentFilterController().redo();
    }

	// Map: R/W
	@Override
	public void undoFilter() {
		FilterController.getCurrentFilterController().undo();
    }

    // Map: RO
    @Override
	public Proxy.Properties getStorage() {
        return new PropertiesProxy(getDelegate(), getScriptContext());
    }

	@Override
	public void evaluateAllFormulas() {
		FormulaUtils.evaluateAllFormulas(getDelegate());
	}


	@Override
	public void evaluateOutdatedFormulas() {
		FormulaUtils.evaluateOutdatedFormulas(getDelegate());
	}

	@Override
	public void addListener(NodeChangeListener listener) {
		NodeChangeListeners.of(Controller.getCurrentModeController(), getDelegate()).add(getScriptContext(), listener);
	}

	@Override
	public void removeListener(NodeChangeListener listener) {
		NodeChangeListeners.of(Controller.getCurrentModeController(), getDelegate()).remove(listener);
	}
}
