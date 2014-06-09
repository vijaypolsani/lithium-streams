package org.streams.compliance.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public final class Configuration<T extends DataSource> extends ConfigurationProperties<T, Metrics, PoolAdapter<T>> {

	public static final long DEFAULT_METRIC_LOG_REPORTER_PERIOD = 5;

	public static class Builder<T extends DataSource> {
		private final String uniqueName;
		private final T targetDataSource;
		private final PoolAdapterBuilder<T> poolAdapterBuilder;
		private final MetricsBuilder metricsBuilder;
		private boolean jmxEnabled = true;
		private long metricLogReporterPeriod = DEFAULT_METRIC_LOG_REPORTER_PERIOD;

		public Builder(String uniqueName, T targetDataSource, MetricsBuilder metricsBuilder,
				PoolAdapterBuilder<T> poolAdapterBuilder) {
			this.uniqueName = uniqueName;
			this.targetDataSource = targetDataSource;
			this.metricsBuilder = metricsBuilder;
			this.poolAdapterBuilder = poolAdapterBuilder;
		}

		public Builder setJmxEnabled(boolean enableJmx) {
			this.jmxEnabled = enableJmx;
			return this;
		}

		public Builder setMetricLogReporterPeriod(long metricLogReporterPeriod) {
			this.metricLogReporterPeriod = metricLogReporterPeriod;
			return this;
		}

		public Configuration<T> build() {
			Configuration<T> configuration = new Configuration<T>(uniqueName, targetDataSource);
			configuration.setJmxEnabled(jmxEnabled);
			configuration.setMetricLogReporterPeriod(metricLogReporterPeriod);
			configuration.metrics = metricsBuilder.build(configuration);
			configuration.poolAdapter = poolAdapterBuilder.build(configuration);
			return configuration;
		}
	}

	private final T targetDataSource;
	private Metrics metrics;
	private PoolAdapter poolAdapter;

	private Configuration(String uniqueName, T targetDataSource) {
		super(uniqueName);
		this.targetDataSource = targetDataSource;
	}

	public T getTargetDataSource() {
		return targetDataSource;
	}

	public Metrics getMetrics() {
		return metrics;
	}

	public PoolAdapter<T> getPoolAdapter() {
		return poolAdapter;
	}
}