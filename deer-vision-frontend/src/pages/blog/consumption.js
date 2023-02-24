import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogConsumptionStartImg from "../../images/blogConsumptionStart.webp"
import blogConsumptionDeviceImg from "../../images/blogConsumptionDevice.webp"
import blogConsumptionSettingsImg from "../../images/blogConsumptionSettings.webp"
import blogConsumptionScene1Img from "../../images/blogConsumptionScene1.webp"
import Chart from "chart.js/auto";

class Consumption extends Component {

    constructor(props) {
        super(props);
        this.scene1Chart = null;
    }

    refreshChart() {
        let ctx = document.getElementById("resultScene1");
        if (this.scene1Chart) {
            this.scene1Chart.destroy();
        }
        this.scene1Chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["40 FPS", "60 FPS", "90 FPS", "120 FPS", "140 FPS", "200 FPS"],
                datasets: [{
                    data: ["157", "157", "157", "157", "157", "157"],
                    label: "Computer idle", borderColor: "#007bff", backgroundColor : "#007bff", fill: true, lineTension: 0
                }, {
                    data: ["21", "28", "93", "115", "134", "186"],
                    label: "Game only (4K/high)",
                    borderColor: "#7bff00", backgroundColor : "#7bff00", fill: true, lineTension: 0
                }]
            },
            options: {
                maintainAspectRatio: true,
                animation: true,
                scales: {
                    y: {min: 0, ticks: {stepSize: 1, callback: function(value) {return value + ' watts';}}},
                    x: {stacked: false}
                },
                plugins: {legend: {display: true}, title: {display: false,}}
            }
        });
    }

    componentDidMount() {
        this.refreshChart();
    }

    render() {
        return (
            <div>
                <h2>Electricity consumption of my game</h2>
                <div className="blog-container">
                    <div className="blog-content">
                        <img className={"blog-img-trans"} src={blogConsumptionStartImg} width={341} height={512} alt={"An electric pole"}/>
                        <p className={"center"}>Have you ever wondered how much electricity a video game consumes? My brain asks this kind of questions and needs an answer.</p>

                        <div className={"blog-sub-title"}>Setup</div>
                        <p>Let's start by checking the setup I used to measure the electricity consumption of my puzzle game.</p>
                        <p>My desktop computer:</p>
                        <ul>
                            <li>CPU: Intel i7-8700K CPU @ 3.70GHz</li>
                            <li>Graphics card: Nvidia 2080 Super</li>
                            <li>RAM: 32Go</li>
                            <li>Disk: 980 PRO PCle 4.0 NVMe M.2 SSD</li>
                            <li>Two screens - 4K</li>
                        </ul>

                        <p>The game (<a className={"text-link"} href={"https://store.steampowered.com/app/2305110/Photon_Engineer/"} target={"_blank"} rel="noreferrer">Photon Engineer</a>):</p>
                        <ul>
                            <li>3D environment but with limited number of models</li>
                            <li>Bottleneck on the GPU (CPU usage: ~20%)</li>
                        </ul>

                        <p>Measurement device:</p>
                        <ul>
                            <li>I plug my computer on a device measuring the electricity consumption in Watt. Nothing fancy here.
                                <img className={"blog-img"} src={blogConsumptionDeviceImg} width={450} height={225} alt={"device to measure the electricity consumption"}/>
                            </li>
                            <li>Consumption of my computer (idle): ~157 Watts</li>
                        </ul>

                        <div className={"blog-sub-title"}>Game settings</div>
                        <p>The game allows to configure a lot of settings directly impacting the performance. Here are the used one during my tests:
                        <ul>
                            <li>Screen resolutions: 4k, 2K, 1080p, etc.</li>
                            <li>FPS limit: limit the number of frames per second (FPS) from 40 to 200</li>
                            <li>Gamma: change the global lighting of the game</li>
                            <li>Graphics quality impacting shadow, lighting, etc.</li>
                        </ul></p>
                        <p><img className={"blog-img"} src={blogConsumptionSettingsImg} width={600} height={383} alt={"configuration des paramètres du jeu"}/></p>

                        <div className={"blog-sub-title"}>Results (scene 1)</div>
                        <p>Let's pickup a scene of my puzzle game and let's start to gather some data with different settings.</p>
                        <p>The 3D scene of the game I used to perform the measurements: <img className={"blog-img"} src={blogConsumptionScene1Img} width={600} height={383} alt={"game scene 1"}/></p>
                        <p>I measured the electricity consumption of the game in <strong>4K</strong> with <strong>high</strong> setting and with different refresh rate: 40 FPS, 60 FPS, 90 FPS, 140 FPS, 200 FPS and unlimited (226 FPS). Here are the results:</p>
                        <canvas id="resultScene1"/>
                        I didn't really know what to expect but one thing surprised me at lot: the game consumes almost 9 times more electricity at 200 FPS compared to 40 FPS.
                    </div>
                </div>
            </div>
        );
    }
}

export default Consumption;
