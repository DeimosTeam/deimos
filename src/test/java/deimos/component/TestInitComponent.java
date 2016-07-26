package deimos.component;

import deimos.Component;
import deimos.listener.OnInit;

public class TestInitComponent extends Component implements OnInit {
    private String text = "Nothing";
    private boolean onInitCalled = false;

    @Override
    public void onInit() {
        text = "onInit";
        onInitCalled = true;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean onInitWasCalled() {
        return onInitCalled;
    }
}
