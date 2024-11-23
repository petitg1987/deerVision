import React, {useCallback, useEffect, useRef, useState} from 'react';
import './visitor-graph.css';
import Chart from 'chart.js/auto';
import {getBackendUrl} from "../../js/access";

export default function VisitorCountryGraph({token}) {

    let visitorCountryChart = useRef(null);
    let windowWidth = useRef(-1);

    const [visitorCountryJson, setVisitorCountryJson] = useState(null);
    const [todayVisitorCountryJson, setTodayVisitorCountryJson] = useState(null);

    const updateVisitorCountryChart = useCallback(() => {
        if (visitorCountryJson === null || todayVisitorCountryJson === null) {
            return null;
        }

        let adjustedTodayVisitorCountryJson = {};
        for (let countryIndex in Object.keys(visitorCountryJson)) {
            let countryString = Object.keys(visitorCountryJson)[countryIndex];
            let todayCount = todayVisitorCountryJson[countryString];
            if (!todayCount) {
                todayCount = 0;
            }
            adjustedTodayVisitorCountryJson[countryString] = todayCount;
        }

        let appsVisitorCountryTab = [];
        let visitorDataset = {
            data: Object.values(visitorCountryJson),
            label: ["Visitor's country (last 40 days)"],
            backgroundColor: "#7bd000",
            fill: false,
            lineTension: 0,
            order: 2 //this dataset is drawn below
        }
        appsVisitorCountryTab.push(visitorDataset);

        let todayVisitorDataset = {
            data: Object.values(adjustedTodayVisitorCountryJson),
            label: ["Visitor's country (today)"],
            backgroundColor: "#ff007b",
            fill: false,
            lineTension: 0,
            order: 1 //this dataset is drawn on top
        }
        appsVisitorCountryTab.push(todayVisitorDataset);

        if (visitorCountryChart.current) {
            visitorCountryChart.current.destroy();
        }

        let ctx = document.getElementById("applicationsVisitorCountryChart");
        visitorCountryChart.current = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: Object.keys(visitorCountryJson),
                datasets: appsVisitorCountryTab
            },
            options: {
                maintainAspectRatio: true,
                animation: true,
                scales: {
                    y: {
                        min: 0,
                        ticks: {
                            stepSize: 1
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.05)'
                        }
                    },
                    x: {
                        stacked: true,
                        grid: {
                            color: 'rgba(255, 255, 255, 0.05)'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true
                    },
                    title: {
                        display: false,
                    }
                }
            }
        });
    }, [todayVisitorCountryJson, visitorCountryJson]);

    const onWindowResize = useCallback( () => {
        if(windowWidth.current !== window.innerWidth){
            windowWidth.current = window.innerWidth;
            updateVisitorCountryChart();
        }
    }, [updateVisitorCountryChart]);

    useEffect(() => {
        windowWidth.current = window.innerWidth;
        window.addEventListener('resize', onWindowResize);
        return () => { //umount
            window.removeEventListener('resize', onWindowResize);
        };
    }, [onWindowResize]);

    useEffect(() => {
        updateVisitorCountryChart();
    }, [visitorCountryJson, todayVisitorCountryJson, updateVisitorCountryChart]);

    useEffect(() => {
        fetch(getBackendUrl() + 'api/admin/visitor-country?retrieveDays=40', {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + token,
                })
            })
            .then(response => response.json())
            .then(json => setVisitorCountryJson(json));

        fetch(getBackendUrl() + 'api/admin/visitor-country?retrieveDays=1', {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + token,
                })
            })
            .then(response => response.json())
            .then(json => setTodayVisitorCountryJson(json));
    }, [token]);

    return (
        <div className={"visitorChart"}>
            <canvas id="applicationsVisitorCountryChart"/>
        </div>
    );
}
