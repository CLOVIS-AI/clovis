package clovis.core

/**
 * Marker interface for optional features, in case common behavior needs to be added in the future.
 */
interface Feature

interface DomainFeature : Feature

interface ProviderFeature : Feature
