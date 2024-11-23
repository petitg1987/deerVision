import React, {Component} from 'react';
import './action-completion-time-graph.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';
import {getBackendUrl} from "../../js/access";

class ActionCompletionTimeGraph extends Component {

    constructor(props) {
        super(props);
        this.state = {levelSelected: '0', actionNameSelected: '', includeSnapshotVal: false};
        this.statChart = null;
        this.handleLevelChange = this.handleLevelChange.bind(this);
        this.handleActionNameChange = this.handleActionNameChange.bind(this);
        this.handleVersionChange = this.handleVersionChange.bind(this);
    }

    handleLevelChange(event) {
        this.setState({levelSelected: event.target.value}, this.refreshActionNames);
    }

    handleActionNameChange(event) {
        this.setState({actionNameSelected: event.target.value}, this.refreshChart);
    }

    handleVersionChange(event) {
        this.setState({includeSnapshotVal: event.target.checked}, this.refreshChart);
    }

    async refreshLevelIds() {
        let levelsSelector = document.getElementById("levelsSelect");
        let levelIdsJson = await getWithToken(getBackendUrl() + 'api/admin/levels/ids?appId=' + this.props.appId, this.props.token);

        let selectedLevelStillValid = false;

        levelIdsJson.forEach(levelNumber => {
            let option = document.createElement("option");
            option.text = "Level " + levelNumber;
            option.value = levelNumber;
            levelsSelector.add(option);

            if (levelNumber === this.state.levelSelected) {
                selectedLevelStillValid = true;
                option.selected = true;
            }
        });

        if (!selectedLevelStillValid) {
            levelsSelector.selectedIndex = 0;
            this.setState({levelSelected: levelsSelector.value}, this.refreshActionNames);
        } else {
            await this.refreshActionNames();
        }
    }

    async refreshActionNames() {
        let actionNamesSelector = document.getElementById("actionNamesSelect");
        let actionNamesJson = await getWithToken(getBackendUrl() + 'api/admin/levels/' + this.state.levelSelected + '/actionNames?appId=' + this.props.appId, this.props.token);

        let selectedActionNameStillValid = false;

        actionNamesSelector.length = 0;
        actionNamesJson.forEach(actionName => {
            let option = document.createElement("option");
            option.text = actionName.replace(/([A-Z])/g, ' $1').trim();
            option.value = actionName;
            actionNamesSelector.add(option);

            if (actionName === this.state.actionNameSelected) {
                selectedActionNameStillValid = true;
                option.selected = true;
            }
        });

        if (!selectedActionNameStillValid) {
            actionNamesSelector.selectedIndex = 0;
            this.setState({actionNameSelected: actionNamesSelector.value}, this.refreshActionNames);
        } else {
            this.refreshChart().then(() => {});
        }
    }

    async refreshChart() {
        let ctx = document.getElementById("actionCompletionTimeChart");
        let completionTimesJson = await getWithToken(getBackendUrl() + 'api/admin/levels/' + this.state.levelSelected + '/completionTimes/' + this.state.actionNameSelected + '?appId=' + this.props.appId + '&includeSnapshot=' + this.state.includeSnapshotVal, this.props.token);

        let minutesTab = [];
        let playerCountData = []

        completionTimesJson.forEach(completionTime => {
            minutesTab.push(completionTime.minute + " min");
            playerCountData.push(completionTime.playerCount);
        });

        if (this.statChart) {
            this.statChart.destroy();
        }

        this.statChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: minutesTab,
                datasets: [{
                    label: this.state.actionNameSelected,
                    data: playerCountData,
                    borderColor: "#7bff00",
                    backgroundColor : "#7bff00",
                    fill: true,
                    lineTension: 0
                }]
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
                        display: false
                    },
                    title: {
                        display: false,
                    }
                }
            }
        });
    }

    async componentDidMount() {
        await this.refreshLevelIds();
    }

    render() {
        return (
            <div className="actionCompletionTimeChart">
                <select id="levelsSelect" onChange={this.handleLevelChange} value={this.state.levelSelected}/>&nbsp;&nbsp;
                <select id="actionNamesSelect" onChange={this.handleActionNameChange} value={this.state.actionNameSelected}/>&nbsp;&nbsp;
                <input type="checkbox" id="completionTimeIncludeSnap" onChange={this.handleVersionChange} checked={this.state.includeSnapshotVal}/><label htmlFor="completionTimeIncludeSnap">Snapshot</label>
                <canvas id="actionCompletionTimeChart"/>
            </div>
        );
    }
}

export default ActionCompletionTimeGraph;