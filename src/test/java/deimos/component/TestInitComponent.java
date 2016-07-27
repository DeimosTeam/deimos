package deimos.component;

import deimos.Component;

public class TestInitComponent extends Component {
    private String onInitText = "Nothing";
    private String customText = "Nothing";

    @Override
    public void onInit() {
        onInitText = "onInit";
    }

    public void setText(String customText) {
        this.customText = customText;
    }

    public String getCustomText() {
        return customText;
    }

    public String getOnInitText() {
        return onInitText;
    }
}
