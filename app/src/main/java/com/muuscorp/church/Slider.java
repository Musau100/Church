package com.muuscorp.church;

/**
 * Created by sydney on 9/28/16.
 */

class Slider {

    private String sliderImageUrl;

    public Slider() {
    }

    Slider(String sliderImageUrl) {
        this.sliderImageUrl = sliderImageUrl;
    }

    String getSliderImageUrl() {
        return sliderImageUrl;
    }

    public void setSliderImageUrl(String sliderImageUrl) {
        this.sliderImageUrl = sliderImageUrl;
    }
}
