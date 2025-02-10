package org.example.java4_asm_backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.java4_asm_backend.model.Share;
import org.example.java4_asm_backend.repository.ShareRepository;

@ApplicationScoped
public class ShareService extends AbstractService<Share, Long, ShareRepository> {
    protected ShareService() {
        super(new ShareRepository());
    }
}
