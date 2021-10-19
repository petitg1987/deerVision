import React, { Component } from 'react';
import News from "./news";
import Games from "./games";
import AboutUs from "./about-us";
import "../pages.css"

class Main extends Component {
    render() {
        return (
            <div>
                <div id="news">
                    <News/>
                </div>
                <div id="games">
                    <Games/>
                </div>
                <div id="about-us">
                    <AboutUs/>
                </div>
            </div>
        );
    }
}

export default Main;
