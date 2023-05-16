import React, { Component } from 'react';
import "../pages.css"
import "./blog.css"
import blogConsumptionStartImg from "../../images/blog/blogConsumptionStart.webp"
import blogConsumptionDeviceImg from "../../images/blog/blogConsumptionDevice.webp"
import blogConsumptionSettingsImg from "../../images/blog/blogConsumptionSettings.webp"
import blogConsumptionSceneImg from "../../images/blog/blogConsumptionScene.webp"
import Chart from "chart.js/auto";

class Consumption extends Component {

    constructor(props) {
        super(props);
        this.scene1Chart = null;
        this.scene1V2Chart = null;
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
                    label: "Computer idle", borderColor: "#aaaaaa", backgroundColor : "#aaaaaa", fill: true, lineTension: 0
                }, {
                    data: ["21", "28", "93", "115", "134", "186"],
                    label: "Game only (4K/high)",
                    borderColor: "#7bff00", backgroundColor : "#7bff00", fill: true, lineTension: 0
                }]
            },
            options: {
                maintainAspectRatio: false,
                animation: true,
                scales: {
                    y: {min: 0, ticks: {stepSize: 1, callback: function(value) {return value + ' watts';}}},
                    x: {stacked: false}
                },
                plugins: {legend: {display: true}, title: {display: false,}}
            }
        });

        let ctxV2 = document.getElementById("resultScene1v2");
        if (this.scene1V2Chart) {
            this.scene1V2Chart.destroy();
        }
        this.scene1V2Chart = new Chart(ctxV2, {
            type: 'bar',
            data: {
                labels: ["40 FPS", "60 FPS", "90 FPS", "120 FPS", "140 FPS", "200 FPS"],
                datasets: [{
                    data: ["157", "157", "157", "157", "157", "157"],
                    label: "Computer idle", borderColor: "#aaaaaa", backgroundColor : "#aaaaaa", fill: true, lineTension: 0
                }, {
                    data: ["21", "28", "93", "115", "134", "186"],
                    label: "Game only (4K/high)",
                    borderColor: "#7bff00", backgroundColor : "#7bff00", fill: true, lineTension: 0
                }, {
                    data: ["7", "10", "16", "19", "23", "74"],
                    label: "Game only (2K/medium)",
                    borderColor: "#ff007b", backgroundColor : "#ff007b", fill: true, lineTension: 0
                }]
            },
            options: {
                maintainAspectRatio: false,
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
                        <p className={"center"}><big>Have you ever wondered how much electricity a video game consumes?</big></p>
                        <p className={"center"}>My brain asks these kinds of questions and I need an answer!</p>

                        <div className={"blog-sub-title"}>Setup</div>
                        <p>Let's start by checking the setup I used to measure the electricity consumption of my puzzle game.</p>

                        <p></p>
                        <p>Desktop computer:</p>
                        <ul>
                            <li><b>CPU</b>: Intel i7-8700K CPU @ 3.70GHz</li>
                            <li><b>Graphics card</b>: Nvidia GeForce 2080 Super</li>
                            <li><b>RAM</b>: 32Go</li>
                            <li><b>Disk</b>: 980 PRO PCle 4.0 NVMe M.2 SSD</li>
                            <li><b>Screens</b>: two with 4K resolution</li>
                        </ul>

                        <p></p>
                        <p>Game (<a className={"text-link"} href={"https://store.steampowered.com/app/2305110?utm_source=website-b1"} target={"_blank"} rel="noreferrer">Photon Engineer</a>):</p>
                        <ul>
                            <li>3D environment with minimalist design</li>
                            <li>Bottleneck on the GPU (CPU usage: ~20%)</li>
                        </ul>

                        <p></p>
                        <p>Measurement device:</p>
                        <ul>
                            <li>I plug my computer on a device measuring the electricity consumption in Watt. Nothing fancy here.
                                <img className={"blog-img"} src={blogConsumptionDeviceImg} width={450} height={225} alt={"device to measure the electricity consumption"}/>
                            </li>
                            <li>Consumption of my computer idle: ~157 Watts</li>
                        </ul>

                        <div className={"blog-sub-title"}>Game settings</div>
                        <p>The game allows you to configure many settings that affect both performance and image quality. Here are the settings I used during my tests:</p>
                        <ul>
                            <li>Screen resolution: 4k, 2K, 1080p, etc.</li>
                            <li>FPS limit: limits the number of frames per second (FPS) from 40 to 200</li>
                            <li>Gamma: change the global brightness of the game</li>
                            <li>Graphics quality impacting mainly the shadow and lighting: low, medium, high</li>
                        </ul>
                        <p><img className={"blog-img"} src={blogConsumptionSettingsImg} width={600} height={383} alt={"game settings"}/></p>

                        <p>Let's pick a scene from my puzzle game and start gathering data with different settings. Here is the 3D scene of the game I used to perform the measurements:</p>
                        <p><img className={"blog-img"} src={blogConsumptionSceneImg} width={600} height={383} alt={"game scene 1"}/></p>

                        <div className={"blog-sub-title"}>Results</div>
                        <p>I measured the electricity consumption of the game with a <strong>4K resolution</strong> in <strong>high quality</strong> and with different refresh rate: 40 FPS, 60 FPS, 90 FPS, 120 FPS, 140 FPS and 200 FPS. Here are the results:</p>
                        <div className={"graph-container"}><canvas id="resultScene1"/></div>
                        <p></p>
                        <p>I didn't really know what to expect but one thing surprised me at lot: the game consumes almost 9 times more electricity at 200 FPS compared to 40 FPS.</p>

                        <p></p>
                        <p>Let's try another set of measurements with the following game settings: <strong>2K resolution</strong> in <strong>medium quality</strong>:</p>
                        <div className={"graph-container"}><canvas id="resultScene1v2"/></div>
                        <p></p>
                        These results blew me away. The difference in image quality between both configurations is not easy to notice, but the difference in electricity consumption is quite significant.
                        <p><small><u>Disclaimer</u>: these results are clearly specific to my configuration and to the game. I guess a lot of factors can produce very different results: laptop vs. desktop computer, game with CPU vs. GPU bottlenecks, etc.</small></p>

                        <div className={"blog-sub-title"}>More experiments</div>
                        <p>I try more measurements but without interesting results:</p>
                        <ul>
                            <li>Brightness/gamma settings: insignificant change in consumption</li>
                            <li>Game in 2K fullscreen and 2K windowed: insignificant change in consumption</li>
                            <li>I try to stress more the CPU with game physics: minor change in consumption</li>
                            <li>Use different scenes from the game: similar results compared to those above</li>
                        </ul>
                        <p></p>
                        <p>It seems that only the amount of work provided to the GPU significantly affects the overall power consumption.</p>
                        <p>I also measured other topics related to gaming:</p>
                        <ul>
                            <li>Watch a stream on Twitch: <strong>~3 watts</strong></li>
                            <li>Record a video with OBS: <strong>~42 watts</strong></li>
                        </ul>
                        <p></p>
                    </div>
                </div>
            </div>
        );
    }
}

export default Consumption;
