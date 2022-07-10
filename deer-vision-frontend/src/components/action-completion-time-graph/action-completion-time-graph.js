import React, {Component} from 'react';
import './action-completion-time-graph.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';

class ActionCompletionTimeGraph extends Component {

    constructor(props) {
        super(props);
        this.state = {levelSelected: '0', includeSnapshotVal: false};
        this.statChart = null;
        this.handleLevelChange = this.handleLevelChange.bind(this);
        this.handleVersionChange = this.handleVersionChange.bind(this);
    }

    handleLevelChange(event) {
        this.setState({levelSelected: event.target.value}, this.refreshChart);
    }

    handleVersionChange(event) {
        this.setState({includeSnapshotVal: event.target.checked}, this.refreshChart);
    }

    async refreshChart() {
        let ctx = document.getElementById("actionCompletionTimeChart");
        let completionTimesJson = await getWithToken(this.props.backendUrl + 'api/admin/levels/' + this.state.levelSelected + '/completionTimes?appId=' + this.props.appId + '&includeSnapshot=' + this.state.includeSnapshotVal, this.props.token);

        let minutesTab = [];
        let dataMap = new Map();

        completionTimesJson.forEach(completionTime => {
            minutesTab.push(completionTime.minute + " min");
            completionTime.actionCompletionCounts.forEach(acc => {
                if (!dataMap.has(acc.actionName)) {
                    dataMap.set(acc.actionName, []);
                }
                dataMap.get(acc.actionName).push(acc.playerCount);
            });
        });

        let colors = ["#7bff00", "#ff7b00", "#7b00ff", "#007bff", "#ff007b", "#00ff7b"];
        let timeDatasets = [];
        dataMap.forEach((data, actionName)=>{
            let dataset = {
                label: actionName,
                data: data,
                borderColor: colors[timeDatasets.length],
                backgroundColor : colors[timeDatasets.length],
                fill: true,
                lineTension: 0
            }
            timeDatasets.push(dataset);
        })

        if (this.statChart) {
            this.statChart.destroy();
        }

        this.statChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: minutesTab,
                datasets: timeDatasets
            },
            options: {
                maintainAspectRatio: true,
                animation: true,
                scales: {
                    y: {
                        min: 0,
                        ticks: {
                            stepSize: 1,
                            callback: function(value) {
                                return value + ' players';
                            }
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
        let levelsSelector = document.getElementById("levelsSelect");
        let levelIdsJson = await getWithToken(this.props.backendUrl + 'api/admin/levels/ids?appId=' + this.props.appId, this.props.token);

        let option = document.createElement("option");
        option.text = "Level 0";
        option.value = "0";
        levelsSelector.add(option);

        levelIdsJson.forEach(levelNumber => {
            if (levelNumber !== 0) {
                let option = document.createElement("option");
                option.text = "Level " + levelNumber;
                option.value = levelNumber;
                levelsSelector.add(option);
            }
        });

        this.refreshChart().then(() => {});
    }

    render() {
        return (
            <div className="actionCompletionTimeChart">
                <select id="levelsSelect" onChange={this.handleLevelChange} value={this.state.levelSelected}/>&nbsp;&nbsp;
                <input type="checkbox" id="completionTimeIncludeSnap" onChange={this.handleVersionChange} checked={this.state.includeSnapshotVal}/><label htmlFor="completionTimeIncludeSnap">Snapshot</label>
                <canvas id="actionCompletionTimeChart"/>
            </div>
        );
    }
}

export default ActionCompletionTimeGraph;