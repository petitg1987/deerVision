import React, {useCallback, useEffect, useRef} from 'react';
import './visitor-graph.css';
import Chart from 'chart.js/auto';
import {getBackendUrl} from "../../js/access";

export default function VisitorCountGraph({token}) {

    let visitorCountChart = useRef(null);
    let windowWidth = useRef(-1);

    const refreshVisitorCountChart = useCallback(async () => {
        let visitorCountRespond = await fetch(getBackendUrl() + 'api/admin/visitor-count?retrieveDays=20', {
            method: "GET",
            headers: new Headers({
                'Authorization': 'Bearer ' + token,
            })
        });
        let visitorCountJson = await visitorCountRespond.json();
        let ctx = document.getElementById("applicationsVisitorCountChart");

        let appsVisitorCountTab = [];
        let dataset = {
            data: Object.values(visitorCountJson),
            label: ["Number of visitors"],
            borderColor: "#007bff",
            fill: false,
            lineTension: 0
        }
        appsVisitorCountTab.push(dataset);

        if (visitorCountChart.current) {
            visitorCountChart.current.destroy();
        }

        visitorCountChart.current = new Chart(ctx, {
            type: 'line',
            data: {
                labels: Object.keys(visitorCountJson),
                datasets: appsVisitorCountTab
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
            await refreshVisitorCountChart();
        }
    }, [refreshVisitorCountChart]);

    useEffect(() => {
        refreshVisitorCountChart().then();
        windowWidth.current = window.innerWidth;

        window.addEventListener('resize', onWindowResize);
        return () => { //umount
            window.removeEventListener('resize', onWindowResize);
        };
    }, [onWindowResize, refreshVisitorCountChart]);

    return (
        <div className={"visitorChart"}>
            <canvas id="applicationsVisitorCountChart"/>
        </div>
    );
}
