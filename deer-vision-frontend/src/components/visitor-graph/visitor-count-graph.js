import React, {useCallback, useEffect, useRef, useState} from 'react';
import './visitor-graph.css';
import Chart from 'chart.js/auto';
import {getBackendUrl} from "../../js/access";

export default function VisitorCountGraph({token}) {

    const visitorCountChart = useRef(null);
    const windowWidth = useRef(-1);

    const [jsonResult, setJsonResult] = useState(null);

    const updateVisitorCountChart = useCallback(() => {
        if (jsonResult === null) {
            return;
        }
        let ctx = document.getElementById("applicationsVisitorCountChart");

        let appsVisitorCountTab = [];
        let dataset = {
            data: Object.values(jsonResult),
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
                labels: Object.keys(jsonResult),
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
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.05)'
                        }
                    },
                    x: {
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
    }, [jsonResult]);

    const onWindowResize = useCallback(() => {
        if(windowWidth.current !== window.innerWidth){
            windowWidth.current = window.innerWidth;
            updateVisitorCountChart();
        }
    }, [updateVisitorCountChart]);

    useEffect(() => {
        windowWidth.current = window.innerWidth;
        window.addEventListener('resize', onWindowResize);
        return () => { //umount
            window.removeEventListener('resize', onWindowResize);
        };
    }, [onWindowResize]);

    useEffect(() => {
        updateVisitorCountChart();
    }, [jsonResult, updateVisitorCountChart]);

    useEffect(() => {
        fetch(getBackendUrl() + 'api/admin/visitor-count?retrieveDays=20', {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + token,
                })
            })
            .then(response => response.json())
            .then(json => setJsonResult(json));
    }, [token]);

    return (
        <div className={"visitorChart"}>
            <canvas id="applicationsVisitorCountChart"/>
        </div>
    );
}
