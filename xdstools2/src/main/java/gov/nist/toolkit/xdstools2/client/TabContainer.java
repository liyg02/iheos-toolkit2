package gov.nist.toolkit.xdstools2.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import gov.nist.toolkit.xdstools2.client.abstracts.AbstractPresenter;
import gov.nist.toolkit.xdstools2.client.event.Xdstools2EventBus;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEvent;
import gov.nist.toolkit.xdstools2.client.util.ClientUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TabContainer {
//	private static TabContainer me = new TabContainer();

	// holds TabBar and currently selected panel from deck
	// TabBar in North section.  Center holds SimpleLayoutPanel. SimpleLayoutPanel
	// holds one element from the deck.
	private static DockLayoutPanel OUTERPANEL = new DockLayoutPanel(Style.Unit.EM);

	private static TabBar TABBAR = new TabBar();

	private static DeckLayoutPanel INNER_DECKPANEL = new DeckLayoutPanel();

	// Each element of TABBAR maps to one element of deck
	private static List<TabContents> deck = new ArrayList<>();

	static {
		OUTERPANEL.addNorth(TABBAR, 4.0);
		OUTERPANEL.add(INNER_DECKPANEL);

		TABBAR.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> selectionEvent) {
				selectTab();
			}
		});
	}

	@Inject
	public TabContainer() {
		GWT.log("Build TabContainer");
	}

	//public static TabContainer instance() { return me; }

	/**
	 * Create a new tab/tool.
	 * @param w - content
	 * @param title - title to appear in the little tab at the top
	 * @param select - should be selected upon creation (ignored)
     */
	public HTML addTab(DockLayoutPanel w, AbstractPresenter presenter, String title, boolean select) {

		w.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
		w.getElement().getStyle().setMarginRight(4, Style.Unit.PX);

		int tabIndex = TABBAR.getTabCount();
		HTML titleHtml = new HTML(title);
		formatTitle(titleHtml);
		TABBAR.addTab(buildTabHeaderWidget(titleHtml, w));

		deck.add(new TabContents(w, presenter));
		TABBAR.selectTab(TABBAR.getTabCount() - 1);
//		TABBAR.addSelectionHandler already calls --> selectTab(); Probably need not be called again.

		Xdstools2.getInstance().resizeToolkit();

		announceOpen(title);
		return titleHtml;
	}

	private static void selectTab() {
		TabContents tc = deck.get(TABBAR.getSelectedTab());
		if (tc.presenter != null)  // null for non-MVP tools
			GWT.log("Tab " +  tc.presenter.getClass().getSimpleName()  + " Selected");
		Widget dockLp = tc.panel;

		if (INNER_DECKPANEL.getWidgetIndex(dockLp)==-1) {
			INNER_DECKPANEL.add(dockLp);
		}else {
			String tabName=TABBAR.getTab(TABBAR.getSelectedTab()).toString().split("<div class=\"gwt-HTML\">")[1].split("</div>")[0];
			((Xdstools2EventBus) ClientUtils.INSTANCE.getEventBus()).fireTabSelectedEvent(tabName);
		}
//		INNER_DECKPANEL.getElement().getStyle().setMargin(4, Style.Unit.PX);
//		GWT.log("Calling showWidget");

		// tell tab it has been revealed so it can refresh if it wants to
		// This is an alternate way to get informed about the environment - the event bus is the other way
		if (tc.presenter != null)
			tc.presenter.reveal();

		INNER_DECKPANEL.showWidget(dockLp);
	}

	private void announceOpen(String title) {
		try {
//			int index = TABPANEL.getWidgetCount() - 1;
			int index = TABBAR.getTabCount() - 1;
			if (Xdstools2.getInstance().getIntegrationEventBus()!=null && index>0) {
				Xdstools2.getInstance().getIntegrationEventBus().fireEvent(new V2TabOpenedEvent(null,title /* this will be the dynamic tab code */,index));
			}
		} catch (Throwable t) {
			Window.alert("V2TabOpenedEvent error: " +t.toString());
		}
	}

	private static void deleteTab(int index) {

	}

	private void formatTitle(HTML titleHtml) {
		String h = titleHtml.getHTML();
		if (h.indexOf(' ') == -1) {
			h = h + " .";
			titleHtml.setHTML(h);
		}
	}

	private Widget buildTabHeaderWidget(HTML titleHtml, final DockLayoutPanel content) {
		HorizontalPanel panel = new HorizontalPanel();
		Anchor x = new Anchor("X");
		x.setStyleName("roundedButton2");
		x.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				GWT.log("Delete tab");
				TabContents tc = findPanelInDeck(content);
				int i = deck.indexOf(tc);
				GWT.log("Delete tab " + i);
				deck.remove(i);
				INNER_DECKPANEL.remove(i);
				TABBAR.removeTab(i);
				i = deck.size() - 1;
				selectTab(i);
			}
		});
		panel.add(x);

		formatTitle(titleHtml);

		panel.add(titleHtml);
		return panel;
	}

	private TabContents findPanelInDeck(DockLayoutPanel panel) {
		for (TabContents tc : deck) {
			if (panel == tc.panel)
				return tc;
		}
		return null;
	}

	public static void setWidth(String width) {
//		TABPANEL.setWidth(width);
	}

	public static void setHeight(String width) {
//		TABPANEL.setHeight(width);
	}

	public static void selectTab(int tabIndex) {
		TABBAR.selectTab(tabIndex);

		INNER_DECKPANEL.showWidget(tabIndex);
		/*
		INNERPANEL.clear();
		INNERPANEL.addTest(deck.get(tabIndex));
		*/

//		TABPANEL.selectTab(tabIndex);
	}

	protected static Widget getTabPanel() {
		return OUTERPANEL;
	}

	protected static int getSelectedTab() {
		return TABBAR.getSelectedTab();
	}
//	protected static Widget getWidget(int tabIndex) {
//		return INNERPANEL.getWidget(tabIndex);
//	}

}
