/*
 * CloudSim Plus: A modern, highly-extensible and easier-to-use Framework for
 * Modeling and Simulation of Cloud Computing Infrastructures and Services.
 * http://cloudsimplus.org
 *
 *     Copyright (C) 2015-2021 Universidade da Beira Interior (UBI, Portugal) and
 *     the Instituto Federal de Educação Ciência e Tecnologia do Tocantins (IFTO, Brazil).
 *
 *     This file is part of CloudSim Plus.
 *
 *     CloudSim Plus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CloudSim Plus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with CloudSim Plus. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cloudbus.cloudsim.brokers;

import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.listeners.EventListener;

/**
 * Keeps track of number of VM creation requests and retries sent by a {@link DatacenterBroker}
 * and enables configuring creation retries.
 * VM creation fails when the broker can't find a suitable Host for placement.
 *
 * @author Manoel Campos da Silva Filho
 * @author sohamchari
 * @since CloudSim Plus 7.3.1
 */
public class VmCreation {

    /**
     * Default number of times the broker will try to recreated failed VMs.
     */
    public static final int DEF_CURRENT_VM_CREATION_RETRIES = 5;

    /** @see #getRetryDelay() */
    private double retryDelay;

    /** @see #getMaxRetries() */
    private int maxRetries;

    /** @see #getRetries() */
    private int retries;

    /** @see #getCreationRequests() */
    private int creationRequests;

    /**
     * Creates an object with default values.
     */
    public VmCreation() {
        this.retryDelay = 5;
        this.maxRetries = DEF_CURRENT_VM_CREATION_RETRIES;
    }

    /**
     * Creates an object with the given values for the attributes.
     * @param retryDelay a delay (in seconds) for the broker to retry allocating VMs
     *                            that couldn't be placed due to lack of suitable active Hosts.
     * @param maxRetries the maximum number of times the broker will try to find a host to create (place) the VM.
     */
    public VmCreation(final double retryDelay, final int maxRetries) {
        this.retryDelay = retryDelay;
        this.maxRetries = maxRetries;
    }

    /**
     * Creates an object with all attributes equal to zero.
     * @return
     */
    public static VmCreation ofZero(){
        return new VmCreation(0, 0);
    }

    /**
     * Gets a delay (in seconds) for the broker to retry allocating VMs
     * that couldn't be placed due to lack of suitable active Hosts.
     *
     * @return
     * <ul>
     *  <li>a value larger than zero to indicate the broker will retry
     *  to place failed VM as soon as new VMs or Cloudlets
     *  are submitted or after the given delay.</li>
     *  <li>otherwise, to indicate failed VMs will be just added to the
     *  {@link DatacenterBroker#getVmFailedList()} and the user simulation have to deal with it.
     *  If the VM has an {@link Vm#addOnCreationFailureListener(EventListener) OnCreationFailureListener},
     *  it will be notified about the failure.</li>
     * </ul>
     */
    public double getRetryDelay() {
        return retryDelay;
    }

    /**
     * Sets a delay (in seconds) for the broker to retry allocating VMs
     * that couldn't be placed due to lack of suitable active Hosts.
     *
     * Setting the attribute as:
     * <ul>
     *  <li>larger than zero, the broker will retry to place failed VM as soon as new VMs or Cloudlets
     *  are submitted or after the given delay.</li>
     *  <li>otherwise, failed VMs will be just added to the {@link DatacenterBroker#getVmFailedList()}
     *  and the user simulation have to deal with it.
     *  If the VM has an {@link Vm#addOnCreationFailureListener(EventListener) OnCreationFailureListener},
     *  it will be notified about the failure.</li>
     * </ul>
     * @param retryDelay the value to set
     */
    public void setRetryDelay(final double retryDelay) {
        this.retryDelay = retryDelay;
    }

    /**
     * Gets the maximum number of times the broker will try to find a host to create (place) the VM.
     * @return
     * @see #getRetries()
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Sets the maximum number of times the broker will try to find a host to create (place) the VM.
     * @param maxRetries value to set
     */
    public void setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * Checks if the broker has to retry allocating VMs
     * that couldn't be placed due to lack of suitable Hosts.
     * @return
     */
    public boolean isRetryFailedVms() {
        return retryDelay > 0 && retries < maxRetries;
    }

    /**
     * Increments the current number of times failed VMs were tried to be recreated.
     */
    public void incCurrentRetries() {
        this.retries++;
    }

    /**
     * Gets the current number of times failed VMs were tried to be recreated.
     * @return
     * @see #getMaxRetries()
     */
    public int getRetries() {
        return retries;
    }

    /**
     * Gets the number of VM creation requests considering all submitted VMs.
     * @return
     */
    public int getCreationRequests() {
        return creationRequests;
    }

    /**
     * Resets the number of VM creation requests to the {@link #DEF_CURRENT_VM_CREATION_RETRIES default number}.
     *
     * @see #incCurrentRetries()
     */
    public void resetCurrentRetries() {
        this.retries = DEF_CURRENT_VM_CREATION_RETRIES;
    }

    /**
     * Increments/decrements the number of VM creation requests.
     * @param value the value to increment/decrement
     * @see #resetCurrentRetries()
     */
    public void incCreationRequests(final int value) {
        this.creationRequests += value;
    }
}
