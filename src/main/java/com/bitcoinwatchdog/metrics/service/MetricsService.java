package com.bitcoinwatchdog.metrics.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bitcoinwatchdog.metrics.client.MetricsApiClient;
import com.bitcoinwatchdog.metrics.model.MVRVZScore;
import com.bitcoinwatchdog.metrics.model.NUPL;
import com.bitcoinwatchdog.metrics.model.PuellMultiple;
import com.bitcoinwatchdog.notification.EmailService;

@Service
public class MetricsService implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		try {
			logger.info("Starting Bitcoin watchdog analysis..");
			checkDailyMetrics();
		} finally {
			logger.info("Analysis completed, shutting down..");
			System.exit(0);
		}

	}

	private static final Logger logger = LoggerFactory.getLogger(MetricsService.class);

	private final EmailService emailService;
	// TODO: ULTRA BULLISH THRESHOLDS
	private static final double PUELL_MULTIPLE_THRESHOLD = 5;// 0.6
	private static final double NUPL_THRESHOLD = 5;// 0.2
	private static final double MVRVZ_THRESHOLD = 5;// 0.5

	
	private final MetricsApiClient metricsApiClient;

	
	public MetricsService(MetricsApiClient metricsApiClient, EmailService emailService) {
		this.metricsApiClient = metricsApiClient;
		this.emailService = emailService;
	}

	public void checkDailyMetrics() {
		// Obtendremos siempre el día anterior porque los indicadores diarios tardan un
		// día entero en generarse
		LocalDate yesterday = LocalDate.now().minusDays(1);
		logger.info("Checking metrics for date: {}", yesterday);

		
		List<PuellMultiple> puellMultipleList = metricsApiClient.getPuellMultiple(yesterday);
		List<MVRVZScore> mvrvzList = metricsApiClient.getMVRVZ(yesterday);
		List<NUPL> nuplList = metricsApiClient.getNUPL(yesterday);

		// Verificamos que tenemos datos para todas las métricas
		if (!puellMultipleList.isEmpty() && !nuplList.isEmpty() && !mvrvzList.isEmpty()) {
			// Evaluamos los datos (tomamos el primer elemento porque esperamos solo uno)
			evaluateMetrics(puellMultipleList.get(0), nuplList.get(0), mvrvzList.get(0));
		} else {
			// Log de advertencia si falta algún dato
			logger.warn("Some metrics data is missing for date: {}", yesterday);
		}
	}

	// Método privado que evalúa las métricas y decide si hay señal
	private void evaluateMetrics(PuellMultiple puell, NUPL nupl, MVRVZScore mvrvz) {

		try {
			// Validar que los valores no sean null
			if (puell.getPuellMultiple() == null || nupl.getNupl() == null || mvrvz.getMvrvz() == null) {
				logger.warn("One or more metrics returned null values");
				return;
			}

			// Convertimos los valores de String a double
			// Nota: En un código más robusto, deberíamos manejar posibles
			// NumberFormatException
			double puellValue = Double.parseDouble(puell.getPuellMultiple());
			double nuplValue = Double.parseDouble(nupl.getNupl());
			double mvrvzValue = Double.parseDouble(mvrvz.getMvrvz());

			// Evaluamos cada métrica contra su umbral
			boolean isPuellBullish = puellValue < PUELL_MULTIPLE_THRESHOLD;
			boolean isNuplBullish = nuplValue < NUPL_THRESHOLD;
			boolean isMvrvzBullish = mvrvzValue < MVRVZ_THRESHOLD;

			// Log de los valores actuales para debugging
			logger.info("Metrics values - Puell: {}, NUPL: {}, MVRV: {}", puellValue, nuplValue, mvrvzValue);

			// Si todas las métricas indican señal alcista, procedemos
			if (isPuellBullish && isNuplBullish && isMvrvzBullish) {
				sendBullishSignal(puell, nupl, mvrvz);
			}
		} catch (NumberFormatException e) {
			logger.error("Error parsing metric values: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error evaluating metrics: {}", e.getMessage());
		}

	}

	// Método que maneja la señal alcista cuando se detecta
	private void sendBullishSignal(PuellMultiple puell, NUPL nupl, MVRVZScore mvrvz) {
		// Creamos un mensaje detallado usando text block (Java 15+)
		String message = String.format("""
				Bullish signal detected!
				Date: %s
				Puell Multiple: %s (threshold: %s)
				NUPL: %s (threshold: %s)
				MVRVZ: %s (threshold: %s)
				""", puell.getDate(), puell.getPuellMultiple(), PUELL_MULTIPLE_THRESHOLD, nupl.getNupl(),
				NUPL_THRESHOLD, mvrvz.getMvrvz(), MVRVZ_THRESHOLD);

		// Registramos la señal
		logger.info(message);
		// TODO: Aquí posteriormente añadiremos la llamada al servicio de notificaciones
		emailService.sendAlert("Bitcoin Alert - Bullish signal", message);
	}

}
