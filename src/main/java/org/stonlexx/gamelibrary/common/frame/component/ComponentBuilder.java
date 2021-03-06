package org.stonlexx.gamelibrary.common.frame.component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.stonlexx.gamelibrary.common.frame.component.impl.StandardFrameComponent;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ComponentBuilder<C extends JComponent> {

    private final C swingComponent;

    private Color backgroundColor;

    private TimeUnit updaterTimeUnit;
    private BiConsumer<C, ComponentBuilder<C>> componentAcceptable;

    private long updaterPeriod;


// ================================================================================================================== //

    /**
     * Создать новый строитель компонента из javax.swing
     * по его классу
     *
     * @param componentClass - класс компонента
     */
    public static <C extends JComponent> ComponentBuilder<C> newBuilder(@NonNull Class<? extends C> componentClass) {
        try {
            return newBuilder(componentClass.getConstructor().newInstance());
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }

        return (ComponentBuilder<C>) newBuilder(new JEmptyComponent());
    }

    /**
     * Создать новый строитель компонента из javax.swing
     * по его java-объекту, клонируя его
     *
     * @param swingComponent - компонент
     */
    public static <C extends JComponent> ComponentBuilder<C> newBuilder(@NonNull C swingComponent) {
        return new ComponentBuilder<>(SerializationUtils.clone(swingComponent));
    }

// ================================================================================================================== //

    public ComponentBuilder<C> add(@NonNull BaseFrameComponent<? extends JComponent> frameComponent,
                                   @NonNull String borderLayout) {

        swingComponent.add(frameComponent.getSwingPanel(), borderLayout);
        return this;
    }

    public ComponentBuilder<C> add(@NonNull BaseFrameComponent<? extends JComponent> frameComponent,
                                   int index) {

        swingComponent.add(frameComponent.getSwingPanel(), index);
        return this;
    }

    public ComponentBuilder<C> add(@NonNull BaseFrameComponent<? extends JComponent> frameComponent) {
        return add(true, frameComponent);
    }

    public ComponentBuilder<C> add(boolean addToPanel, @NonNull BaseFrameComponent<? extends JComponent> frameComponent) {
        swingComponent.add(addToPanel ? frameComponent.getSwingPanel() : frameComponent.getSwingComponent());

        return this;
    }

    public ComponentBuilder<C> add(@NonNull Component component,
                                   @NonNull String borderLayout) {

        swingComponent.add(component, borderLayout);
        return this;
    }

    public ComponentBuilder<C> add(@NonNull Component component,
                                   int index) {

        swingComponent.add(component, index);
        return this;
    }

    public ComponentBuilder<C> add(@NonNull Component component) {
        swingComponent.add(component);

        return this;
    }

    public ComponentBuilder<C> name(@NonNull String componentName) {
        swingComponent.setName(componentName);

        return this;
    }

    public ComponentBuilder<C> location(@NonNull Point pointLocation) {
        swingComponent.setLocation(pointLocation);

        return this;
    }

    public ComponentBuilder<C> location(int x, int y) {
        swingComponent.setLocation(x, y);

        return this;
    }

    public ComponentBuilder<C> alignmentX(float alignmentX) {
        swingComponent.setAlignmentX(alignmentX);

        return this;
    }

    public ComponentBuilder<C> alignmentY(float alignmentY) {
        swingComponent.setAlignmentY(alignmentY);

        return this;
    }

    public ComponentBuilder<C> size(Dimension dimension) {
        swingComponent.setSize(dimension);

        return this;
    }

    public ComponentBuilder<C> size(int width, int height) {
        swingComponent.setSize(width, height);

        return this;
    }

    public ComponentBuilder<C> background(@NonNull Color backgroundColor) {
        this.backgroundColor = backgroundColor;

        return this;
    }

    public ComponentBuilder<C> color(@NonNull Color foregroundColor) {
        swingComponent.setForeground(foregroundColor);

        return this;
    }

    public ComponentBuilder<C> font(@NonNull Font font) {
        swingComponent.setFont(font);

        return this;
    }

    public ComponentBuilder<C> fontSize(int fontSize) {
        Font font = swingComponent.getFont();
        Font newFont = new Font(font.getFontName(), font.getStyle(), fontSize);

        return font(newFont);
    }

    public ComponentBuilder<C> autoUpdater(@NonNull TimeUnit timeUnit, long updatePeriod) {
        this.updaterTimeUnit = timeUnit;
        this.updaterPeriod = updatePeriod;

        return this;
    }

    public ComponentBuilder<C> click(@NonNull BaseFrameComponentClickConsumer componentClickConsumer) {
        BaseFrameComponent.KEY_LISTENER_COMPONENTS
                .put(swingComponent, componentClickConsumer);

        return this;
    }

    public ComponentBuilder<C> buttonAction(@NonNull BaseFrameButtonClickConsumer buttonClickConsumer) {
        if (!(swingComponent instanceof AbstractButton)) {
            return this;
        }

        ((AbstractButton) swingComponent)
                .addActionListener(event -> buttonClickConsumer.accept(swingComponent, event));

        return this;
    }

    public ComponentBuilder<C> accept(@NonNull BiConsumer<C, ComponentBuilder<C>> componentAcceptable) {
        (this.componentAcceptable = componentAcceptable).accept(swingComponent, this);

        return this;
    }


    public BaseFrameComponent<C> build() {
        BaseFrameComponent<C> baseFrameComponent = new StandardFrameComponent<>(swingComponent);
        baseFrameComponent.initialize();

        baseFrameComponent.setComponentAcceptable(componentAcceptable);

        baseFrameComponent.getSwingPanel().setBackground(backgroundColor);
        baseFrameComponent.getSwingPanel().setAlignmentX(swingComponent.getAlignmentX());
        baseFrameComponent.getSwingPanel().setAlignmentY(swingComponent.getAlignmentY());

        swingComponent.setAlignmentX(0);
        swingComponent.setAlignmentY(0);

        if (updaterTimeUnit != null) {
            baseFrameComponent.startAutoUpdate(updaterTimeUnit, updaterPeriod);
        }

        return baseFrameComponent;
    }

}
