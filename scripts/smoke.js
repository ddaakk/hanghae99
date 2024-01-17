// docker-compose up -d influxdb grafana
// k6 run --out influxdb=http://127.0.0.1:8086 smoke.js

import http from 'k6/http';
import { check, group, sleep, fail } from 'k6';

export const options = {
    vus: 1, // 1 user looping for 1 minute
    duration: '10s',

    thresholds: {
        http_req_duration: ['p(99)<100'], // 99% of requests must complete below 1.5s
    },
};

export default function () {

}
