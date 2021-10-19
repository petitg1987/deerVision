import React, { Component } from 'react';
import News from "./news";
import Games from "./games";
import AboutUs from "./about-us";
import "../pages.css"
import "./main.css"

class Main extends Component {
    render() {
        return (
            <div>
                <div id="news"/>
                <News/>
                <div id="games"/>
                <Games/>
                <div id="aboutUs"/>
                <AboutUs/>
            </div>
        );
    }
}

export default Main;
