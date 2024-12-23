package com.bitcoinwatchdog.metrics.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bitcoinwatchdog.metrics.client.MetricsApiClient;
import com.bitcoinwatchdog.metrics.model.MVRVZScore;
import com.bitcoinwatchdog.metrics.model.NUPL;
import com.bitcoinwatchdog.metrics.model.PuellMultiple;

@Service
public class MetricsService {

	private static final Logger logger = LoggerFactory.getLogger(MetricsService.class);
	
	
	
		private static final double PUELL_MULTIPLE_THRESHOLD = 0.6;
	    private static final double NUPL_THRESHOLD = 0.2;
	    private static final double MVRV_THRESHOLD = -0.5;
	
	// Inyección de dependencias: Spring inyectará automáticamente una instancia de MetricsApiClient
    // Esta es una buena práctica porque:
    // 1. Hace el código más testeable
    // 2. Sigue el principio de inversión de dependencias
	private final MetricsApiClient metricsApiClient;
	
	
	
 // Constructor que Spring usará para inyectar las dependencias
    // Al ser el único constructor, no necesita @Autowired (es implícito)
	public MetricsService(MetricsApiClient metricsApiClient) {
		this.metricsApiClient = metricsApiClient;
	}
	
	public void checkDailyMetrics() {
		// Obtendremos siempre el día anterior porque los indicadores diarios tardan un día entero en generarse
		LocalDate yesterday = LocalDate.now().minusDays(1);
		logger.info("Checking metrics for date: {}", yesterday);
		
		// La API está diseñada de tal forma que devuelve arrays de objetos
		List<PuellMultiple> puellMultipleList= metricsApiClient.getPuellMultiple(yesterday);
		List<MVRVZScore> mvrvzList= metricsApiClient.getMVRVZ(yesterday);
		List<NUPL> nuplList= metricsApiClient.getNUPL(yesterday);
		
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
	private void evaluateMetrics(PuellMultiple puell, NUPL nupl, MVRVZ mvrvz) {
		// Convertimos los valores de String a double
        // Nota: En un código más robusto, deberíamos manejar posibles NumberFormatException
        double puellValue = Double.parseDouble(puell.getPuellMultiple());
        double nuplValue = Double.parseDouble(nupl.getNupl());
        double mvrvValue = Double.parseDouble(mvrv.getMvrv());

        // Evaluamos cada métrica contra su umbral
        boolean isPuellBullish = puellValue < PUELL_MULTIPLE_THRESHOLD;
        boolean isNuplBullish = nuplValue < NUPL_THRESHOLD;
        boolean isMvrvBullish = mvrvValue < MVRV_THRESHOLD;

	}
	
}
