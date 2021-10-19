import React, { Component, Suspense } from 'react';
import "../pages.css"
import "./main.css"

const News = React.lazy(() => import('./news'));
const Games = React.lazy(() => import('./games'));
const AboutUs = React.lazy(() => import('./about-us'));

class Main extends Component {
    render() {
        return (
            <div>
                <div id="news"/>
                <Suspense fallback={<div>News loading...</div>}>
                    <News/>
                </Suspense>
                <div id="games"/>
                <Suspense fallback={<div>Games loading...</div>}>
                    <Games/>
                </Suspense>
                <div id="aboutUs"/>
                <Suspense fallback={<div>About loading...</div>}>
                    <AboutUs/>
                </Suspense>
            </div>
        );
    }
}

export default Main;
