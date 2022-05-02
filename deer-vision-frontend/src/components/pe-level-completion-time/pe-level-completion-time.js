import React, {Component} from 'react';
import './pe-level-completion-time.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';

class PeLevelCompletionTime extends Component {

    async componentDidMount() {
        let lctJson = await getWithToken(this.props.backendUrl + 'api/admin/levels/1/completionTimes', this.props.token);
        let ctx = document.getElementById("applicationsPeLevelCompletionTimeChart");

        let minutesTab = [];
        let completionTimeTab = [];
        lctJson.forEach(appUsage => {
            minutesTab.push(appUsage.minute);
            completionTimeTab.push(appUsage.quantity);
        });

        let dataset = {
                data: completionTimeTab,
                borderColor: "#7bd000",
            margin: 0,
                backgroundColor : "#7bd000",
                fill: true,
                lineTension: 0
            }

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: minutesTab,
                datasets: [dataset]
            },
            options: {
                maintainAspectRatio: true,
                animation: true,
                plugins: {
                    legend: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: "Level Completion Time"
                    }
                }
            }
        });
    }

    render() {
        return (
            <div>
                <canvas className="chart" id="applicationsPeLevelCompletionTimeChart"/>
            </div>
        );
    }
}

export default PeLevelCompletionTime;