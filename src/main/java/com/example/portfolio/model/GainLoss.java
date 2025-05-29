package com.example.portfolio.model;
import jakarta.persistence.*;
	@Entity
	@Table(name="gain_loss")
	public class GainLoss {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long id;
		
		@ManyToOne
		@JoinColumn(name="holding_id",nullable=false)
		private Holding holding;
		
		@Column(name="gain")
		private double gain;
		
		@Column(name="percentage")
		private double percentage;
		
		public GainLoss() {}
		public GainLoss(Holding holding,double gain,double percentage) {
			this.holding=holding;
			this.gain=gain;
			this.percentage=percentage;
		}
		public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Holding getHolding() {
	        return holding;
	    }

	    public void setHolding(Holding holding) {
	        this.holding = holding;
	    }

	    public double getGain() {
	        return gain;
	    }

	    public void setGain(double gain) {
	        this.gain = gain;
	    }

	    public double getPercentage() {
	        return percentage;
	    }

	    public void setPercentage(double percentage) {
	        this.percentage = percentage;
	    }
	}
