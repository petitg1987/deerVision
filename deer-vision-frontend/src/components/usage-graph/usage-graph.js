import React, {Component} from 'react';
import './usage-graph.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';

class UsageGraph extends Component {

    constructor(props) {
        super(props);
        this.state = {dayValueSelected: '15'};
        this.usageChart = null;
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({dayValueSelected: event.target.value});
        this.refreshChart(event.target.value).then(() => {});
    }

    async refreshChart(dayValueSelected) {
        let usageJson = await getWithToken(this.props.backendUrl + 'api/admin/usage?retrieveDays=' + dayValueSelected, this.props.token);
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
        this.refreshChart(this.state.dayValueSelected).then(() => {});
    }

    render() {
        return (
            <div className="usageChart">
                <select id="daysSelect" onChange={this.handleChange} value={this.state.dayValueSelected}>
                    <option value="15">Last 15 days</option>
                    <option value="30">Last 30 days</option>
                    <option value="60">Last 60 days</option>
                    <option value="90">Last 90 days</option>
                </select>
                <canvas id="applicationsUsageChart"/>
            </div>
        );
    }
}

export default UsageGraph;