/*
 * Decompiled with CFR 0.150.
 */
package org.reflections.scanners;

import java.util.List;
import org.reflections.scanners.AbstractScanner;

public class FieldAnnotationsScanner
extends AbstractScanner {
    @Override
    public void scan(Object cls) {
        String className = this.getMetadataAdapter().getClassName(cls);
        List fields = this.getMetadataAdapter().getFields(cls);
        for (Object field : fields) {
            List<String> fieldAnnotations = this.getMetadataAdapter().getFieldAnnotationNames(field);
            for (String fieldAnnotation : fieldAnnotations) {
                if (!this.acceptResult(fieldAnnotation)) continue;
                String fieldName = this.getMetadataAdapter().getFieldName(field);
                this.getStore().put((Object)fieldAnnotation, (Object)String.format("%s.%s", className, fieldName));
            }
        }
    }
}

