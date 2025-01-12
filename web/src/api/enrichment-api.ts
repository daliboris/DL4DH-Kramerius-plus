import { EnrichmentRequest } from 'models/enrichment-request/enrichment-request'
import { EnrichmentRequestFilterDto } from 'models/enrichment-request/enrichment-request-filter-dto'
import { EnrichmentJobEventConfig } from 'models/job/config/enrichment/enrichment-job-event-config'
import { QueryResults } from 'models/query-results'
import { customFetch } from 'utils/custom-fetch'

export const enrich = async (
	publicationIds: string[],
	configs: EnrichmentJobEventConfig[],
	name?: string,
): Promise<Response> => {
	return await customFetch('/api/enrichment', {
		method: 'POST',
		body: JSON.stringify({ publicationIds, name, configs }),
	})
}

export const listEnrichmentRequests = async (
	page: number,
	pageSize: number,
	filter?: EnrichmentRequestFilterDto,
): Promise<QueryResults<EnrichmentRequest>> => {
	const filterCopy = { ...filter }

	const filterParams = filterCopy
		? Object.entries(filterCopy)
				.map(([key, value]) => (value ? `&${key}=${value}` : ''))
				.join('')
		: ''
	const url = `/api/enrichment/list?page=${page}&pageSize=${pageSize}${filterParams}`

	const response = await customFetch(url, {
		method: 'GET',
	})

	return await response.json()
}

export const getEnrichmentRequest = async (
	requestId: string,
): Promise<EnrichmentRequest> => {
	const requestUrl = `/api/enrichment/${requestId}`

	const response = await customFetch(requestUrl, {
		method: 'GET',
	})

	return await response.json()
}
