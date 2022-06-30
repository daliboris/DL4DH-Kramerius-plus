import { Grid, Paper, Button, makeStyles, Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import { GridRowParams } from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { JobExecution, JobEvent } from 'models'
import { useEffect, useState } from 'react'
import { getJobEvent, restartJobExecution } from '../job-api'
import { JobExecutionList } from '../job-execution/job-execution-list'
import { JobExecutionDetail } from '../job-execution/job-execution-detail'
import { toast } from 'react-toastify'
import { useHistory } from 'react-router'

type Props = {
	jobEventId: string
}

const useStyles = makeStyles(() => ({
	paper: {
		padding: '20px',
	},
	button: {
		textTransform: 'none',
		padding: '6px 10px',
	},
}))

export const JobEventDetail = ({ jobEventId }: Props) => {
	const classes = useStyles()
	const { replace } = useHistory()
	const [job, setJob] = useState<JobEvent>()
	const [lastRender, setLastRender] = useState<number>(Date.now())
	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()

	useEffect(() => {
		async function fetchJobDetail(jobId: string) {
			const jobEvent = await getJobEvent(jobId)
			setJob(jobEvent)
		}

		fetchJobDetail(jobEventId)
		setSelectedExecution(undefined)
	}, [jobEventId, lastRender])

	const handleExecutionClick = (params: GridRowParams) => {
		if (job) {
			const jobExecution = job.executions.find(
				exec => exec.id === params.row['id'],
			)
			setSelectedExecution(jobExecution)
		}
	}

	const handleRestart = () => {
		async function doRestart() {
			if (job) {
				const response = await restartJobExecution(job.id) // show notification that restart called successfully
				if (response.ok) {
					toast('Operace proběhla úspěšně', {
						type: 'success',
					})
				}
			}
		}
		doRestart()
		setLastRender(Date.now())
	}

	const onPublicationButtonClick = () => {
		if (job) {
			replace(`/publications/${job.publicationId}`)
		}
	}

	const onRefreshClick = () => {
		setLastRender(Date.now())
	}

	return (
		<Paper className={classes.paper} elevation={4}>
			{job && (
				<Grid container direction="column" spacing={3}>
					<Grid item xs>
						<Box display="flex" flexDirection="row">
							<Box width="80%">
								<ReadOnlyField label="ID" value={'' + job?.id} />
								<ReadOnlyField label="Název úlohy" value={job?.jobName} />
								<ReadOnlyField
									label="ID Publikace"
									value={job?.publicationId}
								/>
								<ReadOnlyField
									label="Typ úlohy"
									value={job?.config.krameriusJob}
								/>
								<ReadOnlyField
									label="Parametre"
									value={Object.entries(job.config.parameters).map(
										([key, value]) => (
											<Grid key={key} container>
												<Grid item xs={4}>
													<Typography variant="body2">{key}</Typography>
												</Grid>
												<Grid item xs={8}>
													<Typography color="primary" variant="body2">
														{JSON.stringify(value)}
													</Typography>
												</Grid>
											</Grid>
										),
									)}
								/>
							</Box>
							<Box sx={{ p: 2 }}>
								<Box sx={{ p: 1 }}>
									<Button
										className={classes.button}
										color="primary"
										variant="contained"
										onClick={onPublicationButtonClick}
									>
										Zobrazit publikaci
									</Button>
								</Box>
								<Box sx={{ p: 1 }}>
									<Button
										className={classes.button}
										color="primary"
										variant="contained"
										onClick={onRefreshClick}
									>
										Znovu načíst
									</Button>
								</Box>
							</Box>
						</Box>
						<Box paddingBottom={2}>
							{job.executions.length > 0 &&
								job.executions[job.executions.length - 1].status ===
									'FAILED' && (
									<Button
										color="primary"
										type="submit"
										variant="contained"
										onClick={handleRestart}
									>
										Restartovat
									</Button>
								)}
						</Box>
					</Grid>
					<Grid item xs>
						<JobExecutionList
							key={lastRender}
							executions={job.executions}
							onRowClick={handleExecutionClick}
						/>
					</Grid>
					{selectedExecution && (
						<Grid item xs>
							<Paper elevation={3}>
								<JobExecutionDetail
									key={lastRender}
									jobExecution={selectedExecution}
								/>
							</Paper>
						</Grid>
					)}
				</Grid>
			)}
		</Paper>
	)
}
