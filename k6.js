// 제일 요청이 많은 예약 API에 10 만명의 사람이 몰렸을 때
// k6 run --out json=result.json k6.js

import http from "k6/http"
import { check, sleep } from "k6"
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { textSummary } from "https://jslib.k6.io/k6-summary/0.0.1/index.js";

export const options = {
    stages: [
        {duration: '5s', target: 50},
        {duration: '10s', target: 50},
        {duration: '5s', target: 100},
        {duration: '10s', target: 100},
        {duration: '5s', target: 150},
        {duration: '10s', target: 150},
    ],
    // scenarios: {
        // generate_tokens: {
        //     executor: 'constant-vus', // 고정 가상 유저
        //     vus: 1000,
        //     duration: '10s',
        //     gracefulStop: '0s',
        //     // preAllocatedVUs: 10, // the size of the VU (i.e. worker) pool for this scenario
        //     tags: { test_type: 'website' },
        //     exec: 'generateTokens',
        // },
        // book_test: {
        //     executor: 'constant-vus', // 고정 가상 유저
        //     vus: 50,
        //     duration: '10s',
        //     gracefulStop: '0s',
        //     // preAllocatedVUs: 10, // the size of the VU (i.e. worker) pool for this scenario
        //     startTime: '10s',
        //     tags: { test_type: 'website' },
        //     exec: 'bookTest',
        // },
    // },
    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors 가 1% 이하이어야함
        http_req_duration: ['p(95)<200'], // 요청의 95% 가 200ms 이하 이어야함
    },
}

const BASE_URL = 'http://localhost:8080'

export default function() {
    let params = { headers: { "Content-Type": "application/json" } }
    const res = http.post(`${BASE_URL}/api/v1/user/token`, JSON.stringify({}), params);

    check(res, {
        'Post status is 201': (r) => res.status === 201,
        'Post code is CREATED': (r) => res.json().code === 'CREATED',
    });

    sleep(Math.random() * 2);
}

// export function bookTest() {
//     const headers = {
//         'Content-Type': 'application/json',
//         'Authorization': uuid
//     };
//     const payload = JSON.stringify({
//         concertNumber: '012345',
//         date: '2023-12-02',
//         seatNumbers: [1, 3],
//     });
//     const res = http.post(`${BASE_URL}/api/v1/book`, payload, { headers });
//
//     check(res, {
//         'Post status is 200': (r) => res.status === 200,
//     });
//
//     sleep(Math.random() * 2);
// }

export function handleSummary(data) {
    return {
        "result.html": htmlReport(data),
        stdout: textSummary(data, { indent: " ", enableColors: true }),
    };
}
