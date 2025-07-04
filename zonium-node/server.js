import express from 'express';
import puppeteer from 'puppeteer-extra';
import StealthPlugin from 'puppeteer-extra-plugin-stealth';
import fetch from 'node-fetch';
import { FingerprintGenerator } from 'fingerprint-generator';
import { FingerprintInjector } from 'fingerprint-injector';
import cors from 'cors';

puppeteer.use(StealthPlugin());

const app = express();

const fingerprintGenerator = new FingerprintGenerator({
    browsers: [
        { name: 'firefox', minVersion: 80 },
        { name: 'chrome', minVersion: 87 },
        'safari',
    ],
    devices: ['desktop'],
    operatingSystems: ['windows'],
});

const fingerprintInjector = new FingerprintInjector();

app.use(cors({
    origin: 'http://localhost:5173',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    credentials: true,
}));
app.use(express.json());

app.post('/open-session', async (req, res) => {
    try {
        const { id, fingerprint, userAgent, proxyId } = req.body;

        console.log('Получен payload:', { id, fingerprint, userAgent, proxyId });

        let proxyConfig = null;

        if (proxyId) {
            const proxyResponse = await fetch(`http://localhost:8080/api/proxy/${proxyId}`);
            if (!proxyResponse.ok) throw new Error(`Failed to fetch proxy config for id=${proxyId}`);
            proxyConfig = await proxyResponse.json();
            console.log('Получен прокси конфиг:', proxyConfig);
        }

        let generatedFingerprint;
        let generatedUserAgent;
        let browserFingerprintWithHeaders;

        if (!fingerprint || !userAgent) {
            const fpData = fingerprintGenerator.getFingerprint();
            generatedFingerprint = fpData.fingerprint;
            generatedUserAgent = fpData.headers['user-agent'] || '';
            browserFingerprintWithHeaders = fpData;
        } else {
            generatedFingerprint = JSON.parse(fingerprint);
            generatedUserAgent = userAgent;
            browserFingerprintWithHeaders = {
                fingerprint: generatedFingerprint,
                headers: { 'user-agent': generatedUserAgent },
            };
        }

        const launchOptions = {
            headless: false,
            args: [],
        };

        if (proxyConfig && proxyConfig.host && proxyConfig.port) {
            const protocol = proxyConfig.type || 'http';
            launchOptions.args.push(`--proxy-server=${protocol}://${proxyConfig.host}:${proxyConfig.port}`);
        }

        const browser = await puppeteer.launch(launchOptions);
        const page = await browser.newPage();

        if (proxyConfig && proxyConfig.username && proxyConfig.password) {
            await page.authenticate({
                username: proxyConfig.username,
                password: proxyConfig.password,
            });
        }

        // Внедряем fingerprint
        await fingerprintInjector.attachFingerprintToPuppeteer(page, browserFingerprintWithHeaders);

        await page.goto('https://amiunique.org/fingerprint');

        if (!fingerprint || !userAgent) {
            await fetch(`http://localhost:8080/api/fingerprint/${id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    fingerprint: JSON.stringify(generatedFingerprint),
                    userAgent: generatedUserAgent,
                }),
            });
        }

        res.json({ success: true, fingerprint: generatedFingerprint, userAgent: generatedUserAgent });

        // await browser.close();
    } catch (err) {
        console.error('Error in /open-session:', err);
        res.status(500).json({ success: false, message: err.message });
    }
});

app.listen(3001, () => {
    console.log('Node.js server listening on port 3001');
});
