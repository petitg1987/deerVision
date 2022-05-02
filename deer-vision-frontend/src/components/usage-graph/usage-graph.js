import React, {Component} from 'react';
import './usage-graph.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';

class UsageGraph extends Component {

    constructor(props) {
        super(props);
        this.state = {dayValueSelected: '15', ignoreSnapshotVal: true};
        this.usageChart = null;
        this.handleDaysChange = this.handleDaysChange.bind(this);
        this.handleVersionChange = this.handleVersionChange.bind(this);
    }

    handleDaysChange(event) {
        this.setState({dayValueSelected: event.target.value}, this.refreshChart);
    }

    handleVersionChange(event) {
        this.setState({ignoreSnapshotVal: event.target.checked}, this.refreshChart);
    }

    async refreshChart() {
        let usageJson = await getWithToken(this.props.backendUrl + 'api/admin/usage?retrieveDays=' + this.state.dayValueSelected + "&ignoreSnapshot=" + this.state.ignoreSnapshotVal, this.props.token);
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

        if (this.usageChart) {
            this.usageChart.destroy();
        }

        this.usageChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: datesTab,
                datasets: appsUsageCountTab
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
    }

    async componentDidMount() {
        this.refreshChart().then(() => {});
    }

    render() {
        return (
            <div className="usageChart">
                <select id="daysSelect" onChange={this.handleDaysChange} value={this.state.dayValueSelected}>
                    <option value="15">Last 15 days</option>
                    <option value="30">Last 30 days</option>
                    <option value="60">Last 60 days</option>
                    <option value="90">Last 90 days</option>
                </select>
                <input type="checkbox" id="usageIgnoreSnap" onChange={this.handleVersionChange} checked={this.state.ignoreSnapshotVal}/><label htmlFor="usageIgnoreSnap">Ignore snapshot</label>
                <canvas id="applicationsUsageChart"/>
            </div>
        );
    }
}

export default UsageGraph;