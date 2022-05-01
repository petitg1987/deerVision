import React, {Component} from 'react';
import './usage-graph.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';

class UsageGraph extends Component {

    async componentDidMount() {
        let usageJson = await getWithToken(this.props.backendUrl + 'api/admin/usage?retrieveDays=30', this.props.token);
        let ctx = document.getElementById("applicationsUsageChart");

        let datesTab = usageJson.dates;
        let appsUsageCountTab = [];
        let colors = ["#007bff", "#ff007b", "#7bd000"];
        let nextColor = 0;
        usageJson.appUsages.forEach(appUsage => {
            let dataset = {
                data: appUsage.usageCounts,
                label: appUsage.appId
                    .replace(/([A-Z])/g, ' $1') //insert a space before all caps
                    .replace(/^./, (str) => str.toUpperCase()),
                borderColor: colors[nextColor++ % 3],
                fill: false,
                lineTension: 0
            }
            appsUsageCountTab.push(dataset);
        })

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: datesTab,
                datasets: appsUsageCountTab
            },
            options: {
                maintainAspectRatio: true,
                animation: true,
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
    }

    render() {
        return (
            <div>
                <canvas className="chart" id="applicationsUsageChart"/>
            </div>
        );
    }
}

export default UsageGraph;