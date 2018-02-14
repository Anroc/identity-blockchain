package de.iosl.blockchain.identity.core.shared.provider;

import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatService;
import de.iosl.blockchain.identity.lib.dto.RegistryEntryDTO;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private HeartBeatService heartBeatService;

    @GetMapping("/{ethID}")
    public RegistryEntryDTO getProviderInformation(@PathVariable("ethID") @NotBlank String ethID) {
        return heartBeatService.discover(ethID).orElseThrow(
                () -> new ServiceException("Could not find any registered provider with ethID [%s]",
                        HttpStatus.NOT_FOUND, ethID)
        );
    }
}
