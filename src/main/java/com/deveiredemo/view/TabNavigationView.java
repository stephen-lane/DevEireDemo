package com.deveiredemo.view;

import com.psddev.dari.util.ObjectUtils;
import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.model.TabNavigationModule;
import com.deveiredemo.view.base.BspLinkView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@HandlebarsTemplate("components/jsg-tab-navigation")
public interface TabNavigationView {

    List<? extends BspLinkView> getLinks();

    static class FromTabNavigationModule extends JsgViewCreator<TabNavigationModule> implements TabNavigationView, ModuleView {

        @Override
        public List<? extends BspLinkView> getLinks() {
            return model.getTabLinks().stream()
                    .map((link) -> new LinkView.TextLink() {
                        @Override
                        public TextView getBody() {
                            return link::getText;
                        }

                        @Override
                        public String getCssClass() {
                            if (ObjectUtils.equals(getMainModel(), link.getHref())) {
                                return "active";
                            }
                            return null;
                        }

                        @Override
                        public Map<String, String> getAttributes() {
                            return LinkView.createAttributes(link);
                        }
                    })
                    .filter((linkView) -> linkView != null)
                    .collect(Collectors.toList());
        }
    }
}
