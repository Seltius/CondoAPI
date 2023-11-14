package pt.iscte.condo.mapper;

import org.mapstruct.Mapper;
import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.domain.Document;

import java.util.Base64;

@Mapper(componentModel = "spring")
public abstract class DocumentMapper {

    public abstract Document documentRequestToDocument(DocumentRequest documentRequest);

    protected byte[] stringToBytes(String string) {
        return string == null ? null : Base64.getDecoder().decode(string);
    }

}
