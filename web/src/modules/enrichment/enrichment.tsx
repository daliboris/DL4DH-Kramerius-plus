import Grid from '@material-ui/core/Grid'

import { EnrichmentForm } from './enrichment-form'

export const Enrichment = () => {
	return (
		<Grid container spacing={2}>
			<Grid item xs={6}>
				<EnrichmentForm />
			</Grid>
		</Grid>
	)
}
