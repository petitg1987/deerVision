import React, {Component} from 'react';
import './pe-level-completion-time-graph.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';

class PeLevelCompletionTimeGraph extends Component {

    constructor(props) {
        super(props);
        this.state = {levelSelected: '0', ignoreSnapshotVal: true};
        this.statChart = null;
        this.handleLevelChange = this.handleLevelChange.bind(this);
        this.handleVersionChange = this.handleVersionChange.bind(this);
    }

    handleLevelChange(event) {
        this.setState({levelSelected: event.target.value}, this.refreshChart);
    }

    handleVersionChange(event) {
        this.setState({ignoreSnapshotVal: event.target.checked}, this.refreshChart);
    }

    async refreshChart() {
        let ctx = document.getElementById("applicationsPeLevelCompletionTimeChart");
        let lctJson = await getWithToken(this.props.backendUrl + 'api/admin/levels/' + this.state.levelSelected + '/completionTimes?ignoreSnapshot=' + this.state.ignoreSnapshotVal, this.props.token);

        let minutesTab = [];
        let playerQuantityTab = [];
        lctJson.forEach(lvlCompTime => {
            minutesTab.push(lvlCompTime.minute + " min");
            playerQuantityTab.push(lvlCompTime.quantity);
        });

        let dataset = {
            data: playerQuantityTab,
            borderColor: "#7bd000",
            backgroundColor : "#7bd000",
            fill: true,
            lineTension: 0
        }

        if (this.statChart) {
            this.statChart.destroy();
        }

        this.statChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: minutesTab,
                datasets: [dataset]
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
        let levelsSelector = document.getElementById("levelsSelect");
        let levelIdsJson = await getWithToken(this.props.backendUrl + 'api/admin/levels/ids', this.props.token);

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
            <div className="levelCompletionTimeChart">
                <select id="levelsSelect" onChange={this.handleLevelChange} value={this.state.levelSelected}/>
                <input type="checkbox" id="levelCTIgnoreSnap" onChange={this.handleVersionChange} checked={this.state.ignoreSnapshotVal}/><label htmlFor="levelCTIgnoreSnap">Ignore snapshot</label>
                <canvas id="applicationsPeLevelCompletionTimeChart"/>
            </div>
        );
    }
}

export default PeLevelCompletionTimeGraph;