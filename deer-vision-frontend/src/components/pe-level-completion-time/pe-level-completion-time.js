import React, {Component} from 'react';
import './pe-level-completion-time.css';
import {getWithToken} from "../../js/request";
import Chart from 'chart.js/auto';

class PeLevelCompletionTime extends Component {

    constructor(props) {
        super(props);
        this.state = {levelSelected: '0'};
        this.statChart = null;
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({levelSelected: event.target.value});
        this.refreshChart(event.target.value).then(() => {});
    }

    async refreshChart(levelSelected) {
        let ctx = document.getElementById("applicationsPeLevelCompletionTimeChart");
        let lctJson = await getWithToken(this.props.backendUrl + 'api/admin/levels/' + levelSelected + '/completionTimes', this.props.token);

        let minutesTab = [];
        let completionTimeTab = [];
        lctJson.forEach(lvlCompTime => {
            minutesTab.push(lvlCompTime.minute);
            completionTimeTab.push(lvlCompTime.quantity);
        });

        let dataset = {
            data: completionTimeTab,
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
        levelIdsJson.forEach(levelNumber => {
            let option = document.createElement("option");
            option.text = "Level " + levelNumber;
            option.value = levelNumber;
            levelsSelector.add(option);
        });

        this.refreshChart(levelIdsJson[0]).then(() => {});
    }

    render() {
        return (
            <div className="levelCompletionTimeChart">
                <select id="levelsSelect" onChange={this.handleChange} value={this.state.levelSelected}/>
                <canvas id="applicationsPeLevelCompletionTimeChart"/>
            </div>
        );
    }
}

export default PeLevelCompletionTime;