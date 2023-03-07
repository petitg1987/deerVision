import React, {useCallback, useEffect, useRef} from 'react';
import './visitor-graph.css';
import Chart from 'chart.js/auto';
import {getBackendUrl} from "../../js/access";

export default function VisitorCountryGraph({token}) {

    let visitorCountryChart = useRef(null);
    let windowWidth = useRef(-1);

    const refreshVisitorCountryChart = useCallback(async () => {
        const retrieveVisitorCount = async (retrieveDays) => {
            let visitorCountryRespond = await fetch(getBackendUrl() + 'api/admin/visitor-country?retrieveDays=' + retrieveDays, {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + token,
                })
            });
            return await visitorCountryRespond.json();
        }

        let visitorCountryJson = await retrieveVisitorCount(40);

        let todayVisitorCountryJson = await retrieveVisitorCount(1);
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
                        }
                    },
                    x: {
                        stacked: true,
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
    }, [token]);

    const onWindowResize = useCallback(async () => {
        if(windowWidth.current !== window.innerWidth){
            windowWidth.current = window.innerWidth;
            await refreshVisitorCountryChart();
        }
    }, [refreshVisitorCountryChart]);

    useEffect(() => {
        refreshVisitorCountryChart().then();
        windowWidth.current = window.innerWidth;

        window.addEventListener('resize', onWindowResize);
        return () => { //umount
            window.removeEventListener('resize', onWindowResize);
        };
    }, [onWindowResize, refreshVisitorCountryChart]);

    return (
        <div className={"visitorChart"}>
            <canvas id="applicationsVisitorCountryChart"/>
        </div>
    );
}
