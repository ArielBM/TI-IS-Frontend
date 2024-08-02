import { LitElement, html } from 'lit';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

class ChartComponent extends LitElement {
    static get properties() {
        return {
            chartId: { type: String },
            chartTitle: { type: String }
        };
    }

    firstUpdated() {
        const ctx = this.shadowRoot.getElementById(this.chartId).getContext('2d');
        this.chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: [],
                datasets: [{
                    label: 'Count',
                    backgroundColor: 'rgba(255, 255, 255, 0.2)',
                    borderColor: 'rgba(255, 255, 255, 1)',
                    borderWidth: 1,
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: 'white'
                        }
                    },
                    x: {
                        ticks: {
                            color: 'white'
                        }
                    }
                },
                plugins: {
                    title: {
                        display: true,
                        text: this.chartTitle || 'Default Title',
                        color: '#ffffff',
                        font: {
                            size: 20
                        }
                    }
                }
            }
        });
    }

    render() {
        return html`<canvas id="${this.chartId}" width="400" height="200"></canvas>`;
    }
}

customElements.define('chart-component', ChartComponent);

window.updateChartData = (chartId, data, labels) => {
    const component = document.querySelector(`chart-component[chartId="${chartId}"]`).chart;
    if (component) {
        component.data.labels = JSON.parse(labels);
        component.data.datasets[0].data = JSON.parse(data);
        component.update();
    } else {
        console.error(`The component or graph to update could not be found.`);
    }
};
