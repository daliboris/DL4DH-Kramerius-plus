package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PublicationMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PublicationMongoWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PublicationMongoPersistentStepFactory extends PersistentStepFactory<Publication, Publication> {

    protected PublicationMongoReader reader;

    protected PublicationMongoWriter writer;

    @Override
    protected ItemReader<Publication> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<Publication> getItemWriter() {
        return writer;
    }

    @Override
    protected int getChunkSize() {
        return 1;
    }

    @Autowired
    public void setReader(PublicationMongoReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setWriter(PublicationMongoWriter writer) {
        this.writer = writer;
    }
}
